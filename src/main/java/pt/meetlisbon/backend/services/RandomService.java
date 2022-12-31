package pt.meetlisbon.backend.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomService {
    public String generate_random_ints(int size) {
        String alphabet = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<size; i++) stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return stringBuilder.toString();
    }
}
