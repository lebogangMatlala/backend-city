package org.city.alert.alert.service.service.imple;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String predictCategory(String description) {
        if(description.toLowerCase().contains("pothole")) {
            return "ROAD";
        }else if (description.toLowerCase().contains("sewer")){
            return "PLUMBING";
        }else if (description.toLowerCase().contains("strike")){
            return "STRIKE";
        }else if (description.toLowerCase().contains("fire")){
            return "FIRE";
        }
        return "GENERAL";
    }

    public int predictPriority(String category) {
        // TODO: ML model or rules
        if("FIRE".equalsIgnoreCase(category) ||
                "PLUMBING".equalsIgnoreCase(category) || "STRIKE".equalsIgnoreCase(category) ) return 1; // highest priority
        return 5;
    }
}
