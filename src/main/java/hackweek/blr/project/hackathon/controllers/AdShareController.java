package hackweek.blr.project.hackathon.controllers;

import hackweek.blr.project.hackathon.model.ClickRequest;
import hackweek.blr.project.hackathon.model.RegistrationData;
import hackweek.blr.project.hackathon.model.ServiceResponse;
import hackweek.blr.project.hackathon.model.ShareRequest;
import hackweek.blr.project.hackathon.service.AdShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdShareController {

    @Autowired
    AdShareService adShareService;

    @PostMapping(path = "/ad/share", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int getShareCount(@RequestBody ShareRequest shareRequest) {
        if(null != shareRequest.getAdvertiseId()) {
            return adShareService.shareCount(shareRequest);
        }
        else {
            return 0;
        }
    }

    @PostMapping(path = "/ad/like", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int getLikeCount(@RequestBody ShareRequest shareRequest) {
        if(null != shareRequest.getAdvertiseId()) {
            return adShareService.likeCount(shareRequest);
        }
        else {
            return 0;
        }
    }

    @PostMapping(path = "/user/uniqueLink", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uniqueReferralLink(@RequestBody ClickRequest uniqueLinkRequest) {

        if(null != uniqueLinkRequest) {
            ServiceResponse serviceResponse = adShareService.generateReferralLink(uniqueLinkRequest);
            if(!serviceResponse.getSuccess()) {
                return new ResponseEntity<>(serviceResponse.getResult(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else {
                return new ResponseEntity<>(serviceResponse.getResult(), HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/user/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> registerUser(@RequestBody RegistrationData userData){

        if(null != userData){
            if(adShareService.register(userData)){
                return new ResponseEntity<>(true, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(false,HttpStatus.CONFLICT);
            }
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/ad/clickEvent", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> clickEvent(@RequestBody ClickRequest click){

        if(null != click){
            if(adShareService.registerClickEvent(click).getSuccess()){
                return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login/verify", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> loginVerify(@RequestBody RegistrationData registrationData){

        if(null != registrationData){
            if(adShareService.verifyLogin(registrationData).getSuccess()){
                return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
