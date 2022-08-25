package com.media.library.controller;

import com.media.library.model.Video;
import com.media.library.payload.request.VideoRequest;
import com.media.library.payload.response.MediaResponse;
import com.media.library.service.FileStorageService;
import com.media.library.service.VideoService;
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
@RequestMapping("/api/v1/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/save")
    public ResponseEntity<?> saveVideo(@RequestParam("file") MultipartFile file,
                                       @RequestParam("title") String title,
                                       @RequestParam("producer") String producer,
                                       @RequestParam("season") String season,
                                       @RequestParam("episode") String episode,
                                       @RequestParam("year") String year,
                                       @RequestParam("resolution") String resolution,
                                       @RequestParam("description") String description,
                                       @RequestParam("duration") String duration) {

        VideoRequest videoRequest = new VideoRequest(title,producer,season,episode,year,resolution,description,duration);
        MediaResponse response= new MediaResponse();
        Video v = videoService.findVideoByTitle(videoRequest.getTitle());
        if (v != null) {
            response.setSuccess(false);
            response.setMessage("Video title already exists");
            response.setData(v);
        }
        else{
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
                response.setData(v);
            }else{
                HashMap<String, String> resultMap = fileStorageService.storeAndRenameFile(file, videoRequest.getTitle());
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
                    Video video = Video.builder().
                            title(videoRequest.getTitle()).
                            producer(videoRequest.getProducer()).
                            season(videoRequest.getSeason()).
                            episode(videoRequest.getEpisode()).
                            year(videoRequest.getYear()).
                            resolution(videoRequest.getResolution()).
                            size(""+fileSize+"").
                            description(videoRequest.getDescription()).
                            format(fileFormat).
                            duration(videoRequest.getDuration()).
                            fileName(fileName).
                            fileURL(fileDownloadUri).
                            uploadDir(fileURL).
                            category(file.getContentType()).
                            views("").
                            downloads("").
                            status("").
                            build();
                    videoService.saveVideo(video);

                    response.setSuccess(true);
                    response.setMessage("Video has been saved successfully!");
                    response.setData(video);
                }
            }

        }
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findVideoByID(@PathVariable("id") Long id){
        Video video = videoService.findVideoByID(id);
        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage( String.format("Retrieving music %s",video.getTitle() ));
        response.setData(video);

        return  ResponseEntity.ok().body(response);
    }
    @GetMapping("/title")
    public ResponseEntity<?> findVideoByTitle(@Valid @RequestParam("title") String title){
        Video  video= videoService.findVideoByTitle(title);
        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage( String.format("Retrieving video %s",video.getTitle() ));
        response.setData(video);

        return  ResponseEntity.ok().body(response);
    }

    @GetMapping("/producer")
    public ResponseEntity<?> findVideoByProducer(@Valid @RequestParam("artist") String producer){
        List<Video> videoList= videoService.findAllVideoByProducer(producer);
        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage(String.format("Retrieving video list for %s",producer));
        response.setData(videoList);

        return  ResponseEntity.ok().body(response);
    }


    @GetMapping("/all")
    public List<Video> fetchAllVideo(){
        return videoService.findAllVideo();
    }

    @DeleteMapping("/{id}")
    public void deleteVideoByID(@PathVariable("id") Long id){
        videoService.deleteVideoByID(id);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateVideo(@RequestParam("file") MultipartFile file,
                                         @RequestParam("title") String title,
                                         @RequestParam("producer") String producer,
                                         @RequestParam("season") String season,
                                         @RequestParam("episode") String episode,
                                         @RequestParam("year") String year,
                                         @RequestParam("resolution") String resolution,
                                         @RequestParam("description") String description,
                                         @RequestParam("duration") String duration) {

        VideoRequest videoRequest = new VideoRequest(title,producer,season,episode,year,resolution,description,duration);

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
                HashMap<String, String> resultMap = fileStorageService.storeAndRenameFile(file, videoRequest.getTitle());
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
                    Video video = Video.builder().
                            title(videoRequest.getTitle()).
                            producer(videoRequest.getProducer()).
                            season(videoRequest.getSeason()).
                            episode(videoRequest.getEpisode()).
                            year(videoRequest.getYear()).
                            resolution(videoRequest.getResolution()).
                            size(""+fileSize+"").
                            description(videoRequest.getDescription()).
                            format(fileFormat).
                            duration(videoRequest.getDuration()).
                            fileName(fileName).
                            fileURL(fileDownloadUri).
                            uploadDir(fileURL).
                            category(file.getContentType()).
                            views("").
                            downloads("").
                            status("").
                            build();
                    videoService.saveVideo(video);

                    response.setSuccess(true);
                    response.setMessage("Video has been saved successfully!");
                    response.setData(video);
                }
            }

        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id,
                                                 HttpServletRequest request) {

        //Long idInt= Long.parseLong(id);
        String fileName = videoService.findVideoByID(id).getFileName();
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

    @PostMapping("/uploadMultipleVideos")
    public ResponseEntity < ? > uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("producer") String producer,
                                                 @RequestParam("season") String season,
                                                 @RequestParam("episode") String episode,
                                                 @RequestParam("year") String year,
                                                 @RequestParam("resolution") String resolution,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("duration") String duration) {
       List<ResponseEntity<?>> list= Arrays.asList(files)
                .stream()
                .map(file -> saveVideo(file, title, producer,season,episode,year,resolution,description,duration))
                .collect(Collectors.toList());

        MediaResponse response= new MediaResponse();
        response.setSuccess(true);
        response.setMessage("Video has been saved successfully!");
        response.setData(list);

        return ResponseEntity.ok().body(response);
    }
}
