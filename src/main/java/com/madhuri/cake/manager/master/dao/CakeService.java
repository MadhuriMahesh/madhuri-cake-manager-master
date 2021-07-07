package com.madhuri.cake.manager.master.dao;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.madhuri.cake.manager.master.utility.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CakeService {

    @Autowired
    protected CakeRepository repository;

    /*
    * Set up the initial data in the in memory database
    * */
    @PostConstruct
    @Transactional
    public void init() throws RecordNotFoundException {
        log.info("parsing cake json");
        try (InputStream inputStream = new URL("https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json").openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }
            log.info("parsing cake json");
            JsonParser parser = new JsonFactory().createParser(buffer.toString());
            if (JsonToken.START_ARRAY != parser.nextToken()) {
                throw new Exception("bad token");
            }

            JsonToken nextToken = parser.nextToken();
            while(nextToken == JsonToken.START_OBJECT) {
                log.info("creating cake entity");

                CakeEntity cakeEntity = new CakeEntity();
                log.info(parser.nextFieldName());
                cakeEntity.setTitle(parser.nextTextValue());

                log.info(parser.nextFieldName());
                cakeEntity.setDescription(parser.nextTextValue());

                log.info(parser.nextFieldName());
                cakeEntity.setImage(parser.nextTextValue());

                repository.save(cakeEntity);

                nextToken = parser.nextToken();
                System.out.println(nextToken);

                nextToken = parser.nextToken();
                System.out.println(nextToken);

            }

        } catch (Exception ex) {
            log.error("Error occured during the parsing and saving the json file: {0} ", ex);
            throw new RecordNotFoundException("Error occured during the parsing and saving the json file" , ex);
        }
        log.info("init finished");
    }

    /*
    * Lists all the cakes
    * */
    public List<CakeEntity> getAllCakes()
    {
        List<CakeEntity> cakeList = repository.findAll();

        if(cakeList.size() > 0) {
            return cakeList;
        } else {
            return new ArrayList<>();
        }
    }

    /*
    * Find the the cakes by userId
    * */
    public List<CakeEntity> findAllByUserId(String name)
    {
        List<CakeEntity> cakeList = repository.findAllByUserId(name);

        if(cakeList.size() > 0) {
            return cakeList;
        } else {
            return new ArrayList<>();
        }
    }

    /*
    * Gets the cake by Id
    *  */
    public CakeEntity getCakeById(Long id) throws RecordNotFoundException
    {
        Optional<CakeEntity> cake = repository.findById(id);

        if(cake.isPresent()) {
            return cake.get();
        } else {
            throw new RecordNotFoundException("No cake record exist for given id");
        }
    }

    /*
    * Creates the cake
    *
    * */
    public CakeEntity createOrUpdateCake(CakeEntity entity) throws RecordNotFoundException
    {
        if(entity.getId() != null) {
            Optional<CakeEntity> cake = repository.findById(entity.getId());
            if(cake.isPresent()) {
                CakeEntity newEntity = cake.get();
                newEntity.setTitle(entity.getTitle());
                newEntity.setDescription(entity.getDescription());
                newEntity.setImage(entity.getImage());
                newEntity.setUser(entity.getUser());

                newEntity = repository.save(newEntity);

                return newEntity;
            }
        }

        entity = repository.save(entity);
        log.info("Cake saved successfully");
        return entity;

    }

    /*
    * Delete the cake
    * */
    public void deleteCakeById(Long id) throws RecordNotFoundException
    {
        Optional<CakeEntity> cake = repository.findById(id);

        if(cake.isPresent())
        {
            repository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No cake record exist for given id");
        }
    }
}
