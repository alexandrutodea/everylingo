package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.UserAlreadyExistsException;
import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.TranslationRequest;
import com.everylingo.everylingoapp.repository.TranslationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TranslationRequestService {
    @Autowired
    private TranslationRequestRepository translationRequestRepository;

    public void saveRequest(TranslationRequest translationRequest) {
        translationRequestRepository.save(translationRequest);
    }

    public List<TranslationRequest> getAllRequests()
    {
        List<TranslationRequest> translationRequests = new ArrayList<TranslationRequest>();
        translationRequestRepository.findAll().forEach(requests1 -> translationRequests.add(requests1));
        return translationRequests;
    }

    public TranslationRequest getRequestById(long id)
    {
        return translationRequestRepository.findById(id).get();
    }

    public void delete(long id)
    {
        translationRequestRepository.deleteById(id);
    }

    public void updateTranslationRequest(TranslationRequest translationRequest) {
        translationRequestRepository.save(translationRequest);
    }
}