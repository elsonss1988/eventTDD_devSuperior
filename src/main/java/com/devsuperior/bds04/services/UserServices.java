package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServices  implements UserDetailsService {

    @Autowired
    private CityRepository repository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private static Logger logger = LoggerFactory.getLogger(UserServices.class);

    @Transactional(readOnly = true)
    public List<CityDTO> findAll(){
        List<City> list = repository.findAll();
        return list.stream().map( x-> new CityDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CityDTO findById(Long id){
        Optional<City> city = repository.findById(id);
        return new CityDTO(city.orElseThrow(()-> new EntityNotFoundException()));
    }

    @Transactional
    public CityDTO update(Long id, CityDTO dto){
        Optional<City> city = repository.findById(id);
        City newCity = city.orElseThrow(()-> new EntityNotFoundException());
        copyDTO(newCity,dto);
        for(Event event: newCity.getEvents()){
            Event eventAdd =eventRepository.getOne(event.getId());
            newCity.getEvents().add(eventAdd);
        }
        return new CityDTO(repository.save(newCity));

    }

    @Transactional
    public CityDTO insert( CityDTO dto){
        City newCity = new City();
        copyDTO(newCity,dto);
        return new CityDTO(repository.save(newCity));
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    private City copyDTO(City newCity, CityDTO dto){
        newCity.setName(dto.getName());
        return newCity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findByEmail(username);
        if(user == null){
            logger.error("User not found: "+ username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("Üser found:"+username);
        return user;
    }
}
