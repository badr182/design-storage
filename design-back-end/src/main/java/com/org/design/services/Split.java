package com.org.design.services;

import com.org.design.entities.Chunks;
import com.org.design.entities.OriginalFile;
import com.org.design.repositories.ChunksRepository;
import com.org.design.repositories.OriginalFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class Split implements SplitService{

    @Autowired
    private OriginalFileRepository originalFileRepo;

    @Autowired
    private ChunksRepository chunksRepo;

    private final static int MB =  1024 * 1024;
    private final static int FOUR_MB_IN_BYTES = MB*10;
    /**
     * @TODO destination: use external source to upload data
     *
     *
     * @param file
     * @throws IOException
     */
    public Flux<Integer> split1(MultipartFile file) throws Exception{

        /*if (file.isEmpty())
            throw new Exception("file is null");

        Path destination = Paths.get("upload-dir")
                .resolve( Paths.get(Objects.requireNonNull(file.getOriginalFilename())) )
                .normalize()
                .toAbsolutePath();
        var tempDirectory =  destination.getParent().toUri();
        List<Chunks> chunks = new ArrayList<>();
        OriginalFile originalFile = new OriginalFile();*/

        Flux<Integer> chunksFlux = Flux.create((FluxSink<Integer> syncChunks) ->{
            try{
                AtomicInteger atomicInteger = new AtomicInteger();
                System.out.println(atomicInteger);
                syncChunks.next(atomicInteger.getAndIncrement());
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
            //TimeUnit.SECONDS.sleep(2);
            /*try (InputStream inputStream = file.getInputStream()){

                final byte[] buffer = new byte[FOUR_MB_IN_BYTES];
                var dataRead = inputStream.read(buffer);
                while (dataRead > -1){
                    Chunks chunksE = new Chunks();
                    File outputFile = File.createTempFile("temp-","-split", new File(tempDirectory));
                    try(FileOutputStream fos = new FileOutputStream(outputFile)){
                        fos.write(buffer, 0, dataRead);
                    }
                    dataRead = inputStream.read(buffer);
                    System.out.println(outputFile.getName());
                    chunksE.setPath(outputFile.getPath());
                    chunksE.setName(outputFile.getName());
                    chunksE.setDateTime(LocalDateTime.now());
                    chunksE.setSize(Files.size(Path.of(outputFile.getPath())));

                    chunks.add(chunksE);


                }
                // Integer i = (int) Files.size(Path.of(outputFile.getPath()));
                syncChunks.next(1);
                TimeUnit.SECONDS.sleep(2);

                originalFile.setName(file.getOriginalFilename());
                originalFile.setName(file.getOriginalFilename());
                originalFile.setChunks(chunks);
                originalFile.setSize((double) file.getSize() / MB);
                originalFile.setType(file.getContentType());
                //syncChunks.complete();
            }catch (IOException | RuntimeException e){
                throw new RuntimeException("Faild to store file",e);
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        });

        var a = 12;
        // return originalFile;
        return chunksFlux;
    }


    /*public Flux<Chunks> split(MultipartFile file) throws Exception{
        BlockingQueue<Chunks> requests = new LinkedBlockingQueue<>();
        //BlockingQueue<SquareResult> replies = new LinkedBlockingQueue<>();
        if (file.isEmpty())
            throw new Exception("file is null");

        Path destination = Paths.get("upload-dir")
                .resolve( Paths.get(Objects.requireNonNull(file.getOriginalFilename())) )
                .normalize()
                .toAbsolutePath();
        var tempDirectory =  destination.getParent().toUri();
        List<Chunks> chunks = new ArrayList<>();
        OriginalFile originalFile = new OriginalFile();
        AtomicInteger atomicInteger = new AtomicInteger();
        //Flux generate sequence
        Runnable runable = () -> {
            try(InputStream inputStream = file.getInputStream()) {
                final byte[] buffer = new byte[FOUR_MB_IN_BYTES];
                var dataRead = inputStream.read(buffer);
                while (dataRead > -1){
                    Chunks chunksE = new Chunks();
                    File outputFile = File.createTempFile("temp-","-split", new File(tempDirectory));
                    try(FileOutputStream fos = new FileOutputStream(outputFile)){
                        fos.write(buffer, 0, dataRead);
                    }
                    dataRead = inputStream.read(buffer);
                    //System.out.println(outputFile.getName());
                    chunksE.setPath(outputFile.getPath());
                    chunksE.setName(outputFile.getName());
                    chunksE.setDateTime(LocalDateTime.now());
                    chunksE.setSize(Files.size(Path.of(outputFile.getPath())));
                    requests.put(chunksE);
                    chunks.add(chunksE);
                    TimeUnit.SECONDS.sleep(8);
                }
                originalFile.setName(file.getOriginalFilename());
                originalFile.setName(file.getOriginalFilename());
                originalFile.setChunks(chunks);
                originalFile.setSize((double) file.getSize() / MB);
                originalFile.setType(file.getContentType());
            }catch (IOException e){

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };

        Thread th = new Thread(runable);
        th.start();

        return Flux.generate((SynchronousSink<Chunks> synchronousSink) -> {
           try{
                System.out.println("Flux generate :" + atomicInteger.getAndIncrement());
                var a = atomicInteger.getAndIncrement();

                TimeUnit.SECONDS.sleep(2);
                synchronousSink.next(requests.take());
                if (a > 21){
                    synchronousSink.complete();
                }

            }catch(InterruptedException e){
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
       });
        // return integerFlux;
        //  Flux<Integer> integerFlux =
    }*/

    public OriginalFile split(MultipartFile file) throws Exception{
        if (file.isEmpty())
            throw new Exception("file is null");

        Path destination = Paths.get("upload-dir")
                .resolve( Paths.get(Objects.requireNonNull(file.getOriginalFilename())) )
                .normalize()
                .toAbsolutePath();
        var tempDirectory =  destination.getParent().toUri();

        List<Chunks> chunks = new ArrayList<>();
        OriginalFile originalFile = new OriginalFile();
        AtomicInteger atomicInteger = new AtomicInteger();
        var size = (double) file.getSize() / MB;
        originalFile.setName(file.getOriginalFilename());
        originalFile.setName(file.getOriginalFilename());
        originalFile.setSize((double) file.getSize() / MB);
        originalFile.setType(file.getContentType());
        var nbchunks = Math.ceil((double) file.getSize()  / FOUR_MB_IN_BYTES);
        originalFile.setNumberChunks((int) Math.ceil(nbchunks));
        originalFileRepo.save(originalFile);

        //Chunks chunks2 = new Chunks();
        //chunks2.setPath("outputFile.getPath()");
        //chunks2.setName("outputFile.getName()");
        //chunks2.setDateTime(LocalDateTime.now());
        //chunks2.setSize(12);
        //chunks2.setOriginalFile(originalFile);
        //chunksRepo.save(chunks2);

        Runnable runable = () -> {
            try(InputStream inputStream = file.getInputStream()) {
                final byte[] buffer = new byte[FOUR_MB_IN_BYTES];
                var dataRead = inputStream.read(buffer);
                while (dataRead > -1){
                    Chunks chunksE = new Chunks();
                    File outputFile = File.createTempFile("temp-","-split", new File(tempDirectory));
                    try(FileOutputStream fos = new FileOutputStream(outputFile)){
                        fos.write(buffer, 0, dataRead);
                    }
                    dataRead = inputStream.read(buffer);
                    chunksE.setPath(outputFile.getPath());
                    chunksE.setName(outputFile.getName());
                    chunksE.setDateTime(LocalDateTime.now());
                    chunksE.setSize(Files.size(Path.of(outputFile.getPath())));
                    chunksE.setOriginalFile(originalFile);
                    chunksRepo.save(chunksE);
                    // originalFile.getChunks().add(chunksE);
                    // originalFileRepo.save(originalFile);
                    TimeUnit.SECONDS.sleep(10);
                }

            }catch (IOException e){

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };

        Thread th = new Thread(runable);
        th.start();
        return  originalFile;

    }

}