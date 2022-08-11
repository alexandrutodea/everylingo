package com.everylingo.everylingoapp.controller;

import com.everylingo.everylingoapp.model.TranslationRequest;
import com.everylingo.everylingoapp.repository.TranslationRequestRepository;
import com.everylingo.everylingoapp.service.TranslationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {
    @Autowired
    private TranslationRequestService translationRequestService;

    @Autowired
    private TranslationRequestRepository translationRequestRepository;

    @GetMapping("/translationrequest")
    private List<TranslationRequest> getAllRequests()
    {
        return translationRequestService.getAllRequests();
    }

    @GetMapping("/translationrequest/{translationrequestid}")
    private TranslationRequest getRequest(@PathVariable("translationrequestid") int translationRequestid)
    {
        return translationRequestService.getRequestById(translationRequestid);
    }

    @DeleteMapping("/translationrequest/{translationrequestid}")
    private void deleteRequest(@PathVariable("translationrequestid") int translationRequestid)
    {
        translationRequestService.delete(translationRequestid);
    }

    @PostMapping(path="/translationrequests")
    public void postOrders(@RequestBody TranslationRequest translationRequest) {
        TranslationRequest request1 = new TranslationRequest(translationRequest.getRequestedBy(), translationRequest.getSourceText(),
                translationRequest.getSourceLanguage(),
                translationRequest.getTargetLanguage());

        translationRequestService.saveRequest(request1);
    }

    @PutMapping(value = "/translationrequest/{id}")
    public void updateRequest(@PathVariable String id, @RequestBody TranslationRequest translationRequest){
        translationRequestService.updateTranslationRequest(translationRequest);
    }
}
