package hackweek.blr.project.hackathon.service;

import hackweek.blr.project.hackathon.model.*;
import hackweek.blr.project.hackathon.repository.AdRepository;
import hackweek.blr.project.hackathon.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdShareService {

    public int shareCount(ShareRequest shareRequest) {

        System.out.println("share: " + AdRepository.AdMap);
        String adId = shareRequest.getAdvertiseId();
        //if map is null or empty
        if (AdRepository.AdMap == null || AdRepository.AdMap.isEmpty() ||
                AdRepository.AdMap.get(adId) == null) {
            AdData adData = new AdData();
            adData.setAdId(adId);
            adData.setShares(1);
            adData.setLikes(0);
            adData.setClicks(0);
            AdRepository.AdMap.put(adId, adData);
            return 1;
        }
        else {
            AdData adData = AdRepository.AdMap.get(adId);
            adData.setShares(adData.getShares() + 1);
            adData.setLikes(adData.getLikes());
            AdRepository.AdMap.put(adId, adData);
            return adData.getShares();
        }
    }

    public int likeCount(ShareRequest shareRequest) {

        System.out.println("like: " + AdRepository.AdMap);
        String adId = shareRequest.getAdvertiseId();
        if (AdRepository.AdMap == null || AdRepository.AdMap.isEmpty() ||
                AdRepository.AdMap.get(adId) == null) {
            AdData adData = new AdData();
            adData.setAdId(adId);
            adData.setShares(0);
            adData.setLikes(1);
            adData.setClicks(0);
            AdRepository.AdMap.put(adId, adData);
            return 1;
        }
        else{
            AdData adData = AdRepository.AdMap.get(adId);
            adData.setShares(adData.getShares());
            adData.setLikes(adData.getLikes() + 1);
            AdRepository.AdMap.put(adId, adData);
            return adData.getLikes();
        }
    }

    public ServiceResponse generateReferralLink(ClickRequest uniqueLinkRequest) {
        String adId = uniqueLinkRequest.getAdvertiseId();
        String userId = uniqueLinkRequest.getUserId();

//        check validity of user or ad
        if (UserRepository.UserMap == null || UserRepository.UserMap.isEmpty() ||
                UserRepository.UserMap.get(uniqueLinkRequest.getUserId()) == null) {
            System.out.println("User not found");
            return new ServiceResponse(false, "User not found");
        }
        if (AdRepository.AdMap == null || AdRepository.AdMap.isEmpty() ||
                AdRepository.AdMap.get(adId) == null) {
            System.out.println("Ad not found");
            AdData adData = new AdData();
            adData.setAdId(adId);
            adData.setShares(0);
            adData.setLikes(0);
            adData.setClicks(0);
            AdRepository.AdMap.put(adId, adData);
        }

        //increase number of shares
        Integer currShares = AdRepository.AdMap.get(adId).getShares();
        AdRepository.AdMap.get(adId).setShares(currShares+1);
        String generatedLink = "http://hostname/share2/userid="+userId+"adCreativeId="+adId;
        return new ServiceResponse(true, generatedLink);

    }

    public boolean register(RegistrationData userData){
        if (UserRepository.UserMap == null || UserRepository.UserMap.isEmpty() ||
                UserRepository.UserMap.get(userData.getUsername()) == null) {
            User user = new User();
            user.setUserEmail(userData.getUsername());
            user.setPassword(userData.getPassword());
            user.setSharedAds(0);
            user.setTotalAdCash(0);
            UserRepository.UserMap.put(userData.getUsername(), user);
            return true;
        }
        else{
            //user already exists
            return false;
        }
    }

    public ServiceResponse registerClickEvent(ClickRequest click) {
        String adId = click.getAdvertiseId();
        String userId = click.getUserId();

        if (UserRepository.UserMap == null || UserRepository.UserMap.isEmpty() ||
                UserRepository.UserMap.get(click.getUserId()) == null) {
            System.out.println("User not found");
            return new ServiceResponse(false, "User not found");
        }
        if (AdRepository.AdMap == null || AdRepository.AdMap.isEmpty() ||
                AdRepository.AdMap.get(adId) == null) {
            System.out.println("Ad not found");
            AdData adData = new AdData();
            adData.setAdId(adId);
            adData.setShares(0);
            adData.setLikes(0);
            adData.setClicks(0);
            AdRepository.AdMap.put(adId, adData);
        }
        //increase number of clicks and update adcash for user who shared
        Integer currClicks = AdRepository.AdMap.get(adId).getClicks();
        AdRepository.AdMap.get(adId).setClicks(currClicks+1);
        Integer updatedCash = UserRepository.UserMap.get(userId).getTotalAdCash() + 1;
        UserRepository.UserMap.get(userId).setTotalAdCash(updatedCash);
        return new ServiceResponse(true, "Adcash Added for "+userId);
    }

    public ServiceResponse verifyLogin(RegistrationData registrationData) {

        String username = registrationData.getUsername();
        String password = registrationData.getPassword();

        if (null != UserRepository.UserMap.get(username)){
            User user = UserRepository.UserMap.get(username);
            if (user.getPassword().equals(password)){
                return new ServiceResponse(true, "Successful!");
            }
        }
       return new ServiceResponse(false, "Either user doesn't exists " +
               "or password is wrong!!");
    }
}