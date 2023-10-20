package com.org.design.controllers;
import com.org.design.entities.Chunks;
import com.org.design.entities.OriginalFile;
import com.org.design.repositories.ChunksRepository;
import com.org.design.repositories.OriginalFileRepository;
import com.org.design.services.SplitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class FileUploadController {

    @Autowired
    private SplitService splitService;

   /*
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        // split file
        // join file
        try {
            // destination
            Path originalName = Paths.get(file.getOriginalFilename());
            Path destination = Paths.get("upload-dir")
                    .resolve( Paths.get(file.getOriginalFilename()) )
                    .normalize()
                    .toAbsolutePath();
            // convert to MB
            Long size = file.getSize(); // bytes
            int MB = 1024 * 1024;  // 1mb = 1024L * 1024L
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            long sizeMB = size / MB;
            // 4MO = bytes ??
            int FOUR_MB_IN_BYTES = MB*4;
            String sizeInMB = decimalFormat.format( (double) size / MB );
            var tempDirectory =  destination.getParent().toUri();
            List<File> list = new ArrayList<>();
            // split file
            try (InputStream inputStream = file.getInputStream()){
                // 4MO in bytes
                final byte[] buffer = new byte[FOUR_MB_IN_BYTES];
                var dataRead = inputStream.read(buffer);
                while (dataRead > -1){
                    File outputFile = File.createTempFile("temp-","-split", new File(tempDirectory));
                    try(FileOutputStream fos = new FileOutputStream(outputFile)){
                        fos.write(buffer, 0, dataRead);
                    }
                    list.add(outputFile);
                    dataRead = inputStream.read(buffer);
                }
                //Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            // join file
            File outputFileJoin = File.createTempFile("temp-","-unisplit",new File(tempDirectory));
            FileOutputStream fos = new FileOutputStream(outputFileJoin);
            for (File f: list){
                Files.copy(f.toPath(), fos);
            }
            fos.close();
            var s = "13";
        }catch (IOException e){
            throw new Exception("Faild to store file",e);
        }
        return "test2";
    }
*/

    /**
     * @TODO: handle responses uqing @ExceptionHandler
     *
     * @param file
     * @return
     * @throws Exception
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/upload/test")
    public ResponseEntity<OriginalFile> uploadTest(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok().body(splitService.split(file));
        // return splitService.split(file);
        /*AtomicInteger atomicInteger = new AtomicInteger();
        //Flux generate sequence
        Flux<Integer> integerFlux = Flux.generate((SynchronousSink<Integer> synchronousSink) -> {
            System.out.println("Flux generate :" + atomicInteger.getAndIncrement());
            try {
                var a = atomicInteger.getAndIncrement();
                TimeUnit.SECONDS.sleep(5);
                synchronousSink.next(a);
                if (a > 30){
                    synchronousSink.complete();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return integerFlux;*/
    }

    @Autowired
    public OriginalFileRepository originalFileRepository;

    @Autowired
    public ChunksRepository chunksRepository;

    /*@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/upload/file/{id}")
    public ResponseEntity<OriginalFile> getFile(@PathVariable long id){
        Optional<OriginalFile> o = originalFileRepository.findById(id);
        if ( !o.isPresent() )  return ResponseEntity.unprocessableEntity().build();
        return ResponseEntity.ok().body(o.get());
    }*/
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/getFile/{id}") // produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public OriginalFile file(@PathVariable long id) throws Exception {

        OriginalFile o = originalFileRepository.findById(id).get();
        //if ( !o.isPresent() )  return ResponseEntity.unprocessableEntity().build();
        /*
        AtomicInteger atomicInteger = new AtomicInteger();
        Flux<OriginalFile> integerFlux = Flux.create((FluxSink<OriginalFile> synchronousSink) -> {
            System.out.println("Flux generate :" + atomicInteger.getAndIncrement());
            try {
                // var a = atomicInteger.getAndIncrement();
                Optional<OriginalFile> o = originalFileRepository.findById(id);
                TimeUnit.SECONDS.sleep(5);
                synchronousSink.next(o.get());
                // if (a > 30){
                //     synchronousSink.complete();
                //}
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return integerFlux;*/
        return o ;//.delayElements(Duration.ofMillis(2500));

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> test() throws Exception {
        // splitService.split(file);
        // return ResponseEntity.ok().body(splitService.split(file));
        // return Flux.interval(Duration.ofSeconds(1))
        //        .map(sequence -> "Flux - " + LocalTime.now().toString());
        AtomicInteger atomicInteger = new AtomicInteger();
        //Flux generate sequence
        Flux<Integer> integerFlux = Flux.generate((SynchronousSink<Integer> synchronousSink) -> {
            System.out.println("Flux generate :" + atomicInteger.getAndIncrement());
            try {
                var a = atomicInteger.getAndIncrement();
                TimeUnit.SECONDS.sleep(5);
                synchronousSink.next(a);
                if (a > 30){
                    synchronousSink.complete();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return integerFlux;

    }

    @GetMapping(path = "/stream/create", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> create() throws Exception {
        // splitService.split(file);
        // return ResponseEntity.ok().body(splitService.split(file));
        // return Flux.interval(Duration.ofSeconds(1))
        //        .map(sequence -> "Flux - " + LocalTime.now().toString());
        AtomicInteger atomicInteger = new AtomicInteger();
        Flux<Integer> integerFlux = Flux.create((FluxSink<Integer> synchronousSink) -> {
            System.out.println("Flux generate :" + atomicInteger.getAndIncrement());
            try {
                var a = atomicInteger.getAndIncrement();
                TimeUnit.SECONDS.sleep(5);
                synchronousSink.next(a);
                if (a > 30){
                    synchronousSink.complete();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return integerFlux;

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> watch() throws Exception {
        BlockingQueue<String> requests = new LinkedBlockingQueue<>();
        Runnable runable = () -> {
            String monitoringDirectory = Paths.get("upload-dir").toString();
            FileAlterationObserver observer = new FileAlterationObserver(monitoringDirectory);

            log.info("Start ACTIVITY, Monitoring " + monitoringDirectory);
            observer.addListener(new FileAlterationListenerAdaptor() {
                @Override
                public void onDirectoryCreate(File file) {
                    try {
                        log.info("New Folder Created:" + file.getName());
                        requests.put("New Folder Created:" + file.getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onDirectoryDelete(File file) {
                    log.info("Folder Deleted:" + file.getName());
                }

                @Override
                public void onFileCreate(File file) {
                    log.info("File Created:" + file.getName() + ": YOUR ACTION");

                }

                @Override
                public void onFileDelete(File file) {
                    log.info("File Deleted:" + file.getName() + ": NO ACTION");
                }
            });
            /* Set to monitor changes for 500 ms */
            FileAlterationMonitor monitor = new FileAlterationMonitor(500, observer);
            try {
                monitor.start();
            } catch (Exception e) {
                log.error("UNABLE TO MONITOR SERVER" + e.getMessage());
                e.printStackTrace();

            }
        };

        Thread th = new Thread(runable);
        th.start();



        AtomicInteger atomicInteger = new AtomicInteger();
        return Flux.create((FluxSink<String> synchronousSink) -> {
            System.out.println("Flux generate :" + atomicInteger.getAndIncrement());
            try {
                var a = atomicInteger.getAndIncrement();
                TimeUnit.SECONDS.sleep(5);
                synchronousSink.next(requests.take());
                if (a > 30){
                    synchronousSink.complete();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // return integerFlux;



    }




    /*
    @GetMapping (value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> download(final HttpServletResponse response) {
        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=sample.zip");
        StreamingResponseBody stream = out -> {
            final String home = System.getProperty("user.home");
            final File directory = new File(home + File.separator + "Documents" + File.separator + "sample");
            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            if(directory.exists() && directory.isDirectory()) {
                try {
                    for (final File file : directory.listFiles()) {
                        final InputStream inputStream=new FileInputStream(file);
                        final ZipEntry zipEntry=new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);
                        byte[] bytes=new byte[1024];
                        int length;
                        while ((length=inputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        inputStream.close();
                    }
                    zipOut.close();
                } catch (final IOException e) {
                    logger.error("Exception while reading and streaming data {} ", e);
                }
            }
        };
        logger.info("steaming response {} ", stream);
        return new ResponseEntity(stream, HttpStatus.OK);
    }
     */
}
