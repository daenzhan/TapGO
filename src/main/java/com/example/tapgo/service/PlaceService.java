package com.example.tapgo.service;

import com.example.tapgo.controller.PlaceController;
import com.example.tapgo.entity.Place;
import com.example.tapgo.repository.PlaceRepository;
import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PlaceService {
    private PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository){
        this.placeRepository=placeRepository;
    }

    // SAVE SERVICE
    public Place save (Place p){
        Place c = placeRepository.save(p);
        return c;
    }

    // SHOW SERVICE
    public List<Place> findAll (){
        return placeRepository.findAll();
    }


    // FIND BY ID
    public Place findByPlaceId (Long place_id){
        Place c = placeRepository.findByPlaceId(place_id);
        return c;
    }


    // UPDATE SERVICE
    // ЖАНИЯ УСТАНОВИ ССЫЛКУ НА ЗАГРУЗКУ
    public boolean updatePlace (Long place_id, String place_name, String location,
                                String category, MultipartFile photo) {
        Place p = placeRepository.findByPlaceId(place_id);
        if (p == null) {
            return false;
        }

        p.setPlaceName(place_name);
        p.setLocation(location);
        p.setCategory(category);

        if (!photo.isEmpty()) {
            try {
                String file_name = photo.getOriginalFilename();
                String upload = "";
                Path file_path = Paths.get(upload + file_name);
                Files.copy(photo.getInputStream(), file_path, StandardCopyOption.REPLACE_EXISTING);
                p.setPhotos("/images/" + file_name);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        placeRepository.save(p);
        return true;
    }


    // DELETE SERVICE
    public boolean deletePlace(Long place_id) {
        Place p = placeRepository.findByPlaceId(place_id);
        if (p == null) {
            return false;
        }

        try {
            placeRepository.delete(p);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
