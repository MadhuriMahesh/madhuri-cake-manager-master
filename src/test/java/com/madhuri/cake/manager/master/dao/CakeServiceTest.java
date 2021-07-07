package com.madhuri.cake.manager.master.dao;

import com.madhuri.cake.manager.master.utility.RecordNotFoundException;
import com.madhuri.cake.manager.master.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CakeServiceTest {

    @Mock
    private CakeRepository repository;

    @InjectMocks
    private CakeService cakeService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void init() throws RecordNotFoundException {
        //setup
        Mockito.when(repository.save(any())).thenReturn(TestUtils.addCake());


        //execute
        cakeService.init();

        //verify
        verify(repository, times(20)).save(any());
    }

    @Test
    public void initException() throws RecordNotFoundException {
        exceptionRule.expect(RecordNotFoundException.class);
        exceptionRule.expectMessage("Error occured during the parsing and saving the json file");
        //setup
        Mockito.when(repository.save(any())).thenThrow(MockitoException.class);

        //execute
        cakeService.init();

    }

    @Test
    public void getAllCakes() {
        //setup
        Mockito.when(repository.findAll()).thenReturn(TestUtils.createCakes());

        //execute
        List<CakeEntity> cakeEntities = cakeService.getAllCakes();

        //verify
        CakeEntity cakeEntity = cakeEntities.get(0);
        Assert.assertEquals("cake1", cakeEntity.getTitle());
        Assert.assertEquals("desc1", cakeEntity.getDescription());
        Assert.assertEquals("img1", cakeEntity.getImage());

    }

    @Test
    public void findAllByUserId() {
        //setup
        Mockito.when(repository.findAllByUserId(any())).thenReturn(TestUtils.createCakes());

        //execute
        List<CakeEntity> cakeEntities = cakeService.findAllByUserId("name");

        //verify
        CakeEntity cakeEntity = cakeEntities.get(0);
        Assert.assertEquals("cake1", cakeEntity.getTitle());
        Assert.assertEquals("desc1", cakeEntity.getDescription());
        Assert.assertEquals("img1", cakeEntity.getImage());
    }

    @Test
    public void getCakeById() throws RecordNotFoundException {
        //setup
        Mockito.when(repository.findById(any())).thenReturn(Optional.of(TestUtils.addCake()));

        //execute
        CakeEntity cakeEntity = cakeService.getCakeById(1L);

        //verify
        Assert.assertEquals("cake1", cakeEntity.getTitle());
        Assert.assertEquals("desc1", cakeEntity.getDescription());
        Assert.assertEquals("img1", cakeEntity.getImage());
    }

    @Test
    public void getCakeByIdException() throws RecordNotFoundException {
        exceptionRule.expect(RecordNotFoundException.class);
        exceptionRule.expectMessage("No cake record exist for given id");
        //setup
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());

        //execute
        cakeService.getCakeById(1L);
    }

    @Test
    public void createOrUpdateCake() throws RecordNotFoundException {
        //setup
        Mockito.when(repository.findById(any())).thenReturn(Optional.of(TestUtils.addCake()));
        Mockito.when(repository.save(any())).thenReturn(TestUtils.addCake());

        //execute
        CakeEntity cakeEntity = cakeService.createOrUpdateCake(TestUtils.addCake());

        //verify
        Assert.assertEquals("cake1", cakeEntity.getTitle());
        Assert.assertEquals("desc1", cakeEntity.getDescription());
        Assert.assertEquals("img1", cakeEntity.getImage());
    }

    @Test
    public void deleteCakeById() throws RecordNotFoundException {
        //setup
        Mockito.when(repository.findById(any())).thenReturn(Optional.of(TestUtils.addCake()));

        //execute
        cakeService.deleteCakeById(1L);

        //verify
        verify(repository).deleteById(1L);
    }

    @Test
    public void deleteCakeByIdException() throws RecordNotFoundException {
        exceptionRule.expect(RecordNotFoundException.class);
        exceptionRule.expectMessage("No cake record exist for given id");
        //setup
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());

        //execute
        cakeService.deleteCakeById(1L);

    }
}