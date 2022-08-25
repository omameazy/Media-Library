package com.media.library.controller;

import com.media.library.model.Music;
import com.media.library.payload.request.MusicRequest;
import com.media.library.payload.response.MediaResponse;
import com.media.library.service.FileStorageService;
import com.media.library.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/music")
public class MusicController {
    @Autowired
    private MusicService musicService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/save")
    public ResponseEntity<?> saveMusic(@RequestParam("file") MultipartFile file,
                                       @RequestParam("title") String title,
                                       @RequestParam("artist") String artist,
                                       @RequestParam("album") String album,
                                       @RequestParam("description") String description,
                                       @RequestParam("duration") String duration) {

        MusicRequest musicRequest = new MusicRequest(title,artist,album,description,duration);

        MediaResponse response= new MediaResponse();
        Music m = musicService.findMusicByTitle(musicRequest.getTitle());
        if(m !=null){
            response.setSuccess(false);
            response.setMessage("Music title already exists!");
            response.setData(m);
        }else{
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";
            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch(Exception e) {
                throw new RuntimeException("Could not obtain file extension", e);
            }
            String fileExt = fileExtension.toLowerCase();
            if(!fileExt.equals(".mp3")&&
                    !fileExt.equals(".wav")&&
                    !fileExt.equals(".wma")&&
                    !fileExt.equals(".3gp")&&
                    !fileExt.equals(".mp4")&&
                    !fileExt.equals(".avi")&&
                    !fileExt.equals(".mkv")&&
                    !fileExt.equals(".wmv")&&
                    !fileExt.equals(".dat")&&
                    !fileExt.equals(".flv")){
                response.setSuccess(false);
                response.setMessage("File type not allowed! Only music and video files are allowed!");
                response.setData(null);
            }else{
                HashMap<String, String> resultMap = fileStorageService.storeAndRenameFile(file, musicRequest.getTitle());
                if(resultMap.isEmpty() ||resultMap==null){
                    response.setSuccess(false);
                    response.setMessage("Sorry! Could not upload file!");
                    response.setData(null);
                }else{
                    String fileName = resultMap.get("fileName");
                    String fileFormat = resultMap.get("fileFormat");
                    String fileUploadDir = resultMap.get("uploadDir");
                    String fileURL = resultMap.get("fileURL");
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/downloadFile/")
                            .path(fileName)
                            .toUriString();

                    double x = (file.getSize()/1024);
                    double size = (x/1024);
                    String sizeString = ""+size+"";
                    String sizeArray[] = sizeString.replace(".", "_").split("_");
                    String fileSize= "";
                    if(sizeArray.length>1){
                        fileSize =sizeArray[0]+"."+(sizeArray[1].substring(0,2))+" MB";
                    }else{
                        fileSize =sizeArray[0]+" MB";
                    }
                    String fileContentType = file.getContentType();
                    Music music = Music.builder().
                            title(musicRequest.getTitle()).
                            artist(musicRequest.getArtist()).
                            album(musicRequest.getAlbum()).
                            description(musicRequest.getDescription()).
                            format(fileFormat).
                            category(fileContentType).
                            duration(musicRequest.getDuration()).
                            fileName(fileName).
                            fileURL(fileDownloadUri).
                            uploadDir(fileURL).
                            views("").
                            downloads("").
                            status("").
                            build();
                    musicService.saveMusic(music);

                    response.setSuccess(true);
                    response.setMessage("Music has been saved successfully!");
                    response.setData(music);
                }
            }

        }

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findMusicByID(@PathVariable("id") Long id){
        Music music = musicService.findMusicByID(id);
        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage( String.format("Retrieving music %s",music.getTitle() ));
        response.setData(music);

        return  ResponseEntity.ok().body(response);
    }
    @GetMapping("/title")
    public ResponseEntity<?> findMusicByTitle(@Valid @RequestParam("title") String title){
        Music  music= musicService.findMusicByTitle(title);
        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage( String.format("Retrieving music %s",music.getTitle() ));
        response.setData(music);

        return  ResponseEntity.ok().body(response);
    }

    @GetMapping("/artist")
    public ResponseEntity<?> findMusicByArtist(@Valid @RequestParam("artist") String artist){
        List<Music> music= musicService.findAllMusicByArtist(artist);
        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage(String.format("Retrieving music list for %s",artist));
        response.setData(music);

        return  ResponseEntity.ok().body(response);
    }


    @GetMapping("/all")
    public List<Music> fetchAllMusic(){
        return musicService.findAllMusic();
    }

    @DeleteMapping("/{id}")
    public void deleteMusicByID(@PathVariable("id") Long id){
        musicService.deleteMusicByID(id);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMusic(@RequestParam("file") MultipartFile file,
                                         @RequestParam("title") String title,
                                         @RequestParam("artist") String artist,
                                         @RequestParam("album") String album,
                                         @RequestParam("description") String description,
                                         @RequestParam("duration") String duration) {

        MusicRequest musicRequest = new MusicRequest(title,artist,album,description,duration);

        MediaResponse response= new MediaResponse();

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";
            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch(Exception e) {
                throw new RuntimeException("Could not obtain file extension", e);
            }
            String fileExt = fileExtension.toLowerCase();
            if(!fileExt.equals(".mp3")&&
                    !fileExt.equals(".wav")&&
                    !fileExt.equals(".wma")&&
                    !fileExt.equals(".3gp")&&
                    !fileExt.equals(".mp4")&&
                    !fileExt.equals(".avi")&&
                    !fileExt.equals(".mkv")&&
                    !fileExt.equals(".wmv")&&
                    !fileExt.equals(".dat")&&
                    !fileExt.equals(".flv")){
                response.setSuccess(false);
                response.setMessage("File type not allowed! Only music and video files are allowed!");
                response.setData(null);
            }else{
                HashMap<String, String> resultMap = fileStorageService.storeAndRenameFile(file, musicRequest.getTitle());
                if(resultMap.isEmpty() ||resultMap==null){
                    response.setSuccess(false);
                    response.setMessage("Sorry! Could not upload file!");
                    response.setData(null);
                }else{
                    String fileName = resultMap.get("fileName");
                    String fileFormat = resultMap.get("fileFormat");
                    String fileUploadDir = resultMap.get("uploadDir");
                    String fileURL = resultMap.get("fileURL");
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/downloadFile/")
                            .path(fileName)
                            .toUriString();

                    double x = (file.getSize()/1024);
                    double size = (x/1024);
                    String sizeString = ""+size+"";
                    String sizeArray[] = sizeString.replace(".", "_").split("_");
                    String fileSize= "";
                    if(sizeArray.length>1){
                        fileSize =sizeArray[0]+"."+(sizeArray[1].substring(0,2))+" MB";
                    }else{
                        fileSize =sizeArray[0]+" MB";
                    }

                    String fileContentType = file.getContentType();
                    Music music = Music.builder().
                            title(musicRequest.getTitle()).
                            artist(musicRequest.getArtist()).
                            album(musicRequest.getAlbum()).
                            description(musicRequest.getDescription()).
                            format(fileFormat).
                            size(""+fileSize+"").
                            category(fileContentType).
                            duration(musicRequest.getDuration()).
                            fileName(fileName).
                            fileURL(fileDownloadUri).
                            uploadDir(fileURL).
                            views("").
                            downloads("").
                            status("").
                            build();
                    musicService.saveMusic(music);

                    response.setSuccess(true);
                    response.setMessage("Music has been saved successfully!");
                    response.setData(music);
                }
            }


        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id,
                                                 HttpServletRequest request) {

        //Long idInt= Long.parseLong(id);
        String fileName = musicService.findMusicByID(id).getFileName();
        Resource resource = null;
        if(fileName !=null && !fileName.isEmpty()) {
            try {
                resource = fileStorageService.loadFileAsResource(fileName);
            } catch (Exception e) {
                throw new RuntimeException("Could not load file as resource", e);
            }
            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                //logger.info("Could not determine file type.");
                throw new RuntimeException("Could not determine file type", ex);
            }
            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/uploadMultipleMusic")
    public ResponseEntity < ? > uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("artist") String artist,
                                                    @RequestParam("album") String album,
                                                    @RequestParam("description") String description,
                                                    @RequestParam("duration") String duration) {
        List<ResponseEntity<?>> list= Arrays.asList(files)
                .stream()
                .map(file -> saveMusic(file, title, artist,album,description,duration))
                .collect(Collectors.toList());

        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage("Video has been saved successfully!");
        response.setData(list);

        return ResponseEntity.ok().body(response);
    }
}
