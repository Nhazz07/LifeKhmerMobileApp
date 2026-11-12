package com.lifekhmer.publicinfo.controller;

import com.lifekhmer.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class PublicContentController {

    @GetMapping("/health-tips")
    public ApiResponse<List<Map<String, String>>> getHealthTips() {
        List<Map<String, String>> tips = List.of(
                Map.of(
                        "id", "tip-1",
                        "title", "Balanced Cambodian Diet",
                        "titleKm", "របបអាហារខ្មែរដែលមានតុល្យភាព",
                        "description", "Pair traditional rice meals with plenty of fresh vegetables like Somlor Korko or water spinach, and limit high-sodium sauces like fish sauce and prahok.",
                        "descriptionKm", "ទទួលទានបាយជាមួយបន្លែស្រស់ៗដូចជាសម្លកកូរ ឬត្រកួន និងកាត់បន្ថយការប្រើប្រាស់ទឹកត្រី និងប្រហុកច្រើនជ្រុល។",
                        "category", "NUTRITION"
                ),
                Map.of(
                        "id", "tip-2",
                        "title", "Stay Hydrated in Tropical Climate",
                        "titleKm", "ការរក្សាជាតិទឹកក្នុងអាកាសធាតុក្តៅ",
                        "description", "Cambodia's tropical weather requires drinking at least 2.5 to 3 liters of water daily, especially during outdoor work or warm seasons.",
                        "descriptionKm", "អាកាសធាតុប្រទេសកម្ពុជាទាមទារឱ្យទទួលទានទឹកយ៉ាងហោចណាស់ ២.៥ ទៅ ៣ លីត្រក្នុងមួយថ្ងៃ ជាពិសេសពេលធ្វើការក្រៅផ្ទះ។",
                        "category", "HYDRATION"
                ),
                Map.of(
                        "id", "tip-3",
                        "title", "Monitoring Blood Pressure Regularly",
                        "titleKm", "ការតាមដានសម្ពាធឈាមជាប្រចាំ",
                        "description", "Hypertension often shows no symptoms. Measure your blood pressure weekly in the morning while rested.",
                        "descriptionKm", "ជំងឺលើសឈាមច្រើនតែមិនបង្ហាញរោគសញ្ញា។ វាស់សម្ពាធឈាមរបស់អ្នករៀងរាល់សប្តាហ៍នៅពេលព្រឹក។",
                        "category", "VITALS"
                ),
                Map.of(
                        "id", "tip-4",
                        "title", "Daily Movement & Walking",
                        "titleKm", "ការហាត់ប្រាណ និងការដើរប្រចាំថ្ងៃ",
                        "description", "Aim for 30 minutes of brisk walking or cycling at local parks to improve heart health and glycemic control.",
                        "descriptionKm", "ដើរលឿន ឬជិះកង់រយៈពេល ៣០ នាទីនៅសួនច្បារ ដើម្បីបង្កើនសុខភាពបេះដូង។",
                        "category", "FITNESS"
                )
        );
        return new ApiResponse<>(true, "Public health tips retrieved", tips);
    }

    @GetMapping("/app-info")
    public ApiResponse<Map<String, Object>> getAppInfo() {
        return new ApiResponse<>(true, "App information retrieved", Map.of(
                "name", "LifeKhmer",
                "tagline", "Smart Health & Nutrition for Cambodia",
                "version", "1.0.0",
                "supportedLanguages", List.of("en", "km"),
                "features", List.of(
                        "Guest Exploration & Food Catalog",
                        "Personal Health & Vitals Tracking",
                        "Nutrient Breakdown & Daily Goals",
                        "AI Food Image Recognition",
                        "Medication Schedules & Reminders",
                        "Doctor Appointments Management",
                        "Symptom Diary"
                )
        ));
    }
}
