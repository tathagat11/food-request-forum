package com.homey.foodforum;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.homey.foodforum.models.ChefResponse;
import com.homey.foodforum.models.FoodRequest;
import com.homey.foodforum.models.Location;
import com.homey.foodforum.models.enums.ResponseStatus;
import com.homey.foodforum.service.ChefResponseService;
import com.homey.foodforum.service.FoodRequestService;

public class Main {
    private static FoodRequestService requestService = new FoodRequestService();
    private static ChefResponseService responseService = new ChefResponseService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Create Food Request");
            System.out.println("2. Add Chef Response");
            System.out.println("3. View Requests");
            System.out.println("4. View Responses");
            System.out.println("5. View Chef Notifications");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createFoodRequest();
                    break;
                case 2:
                    addChefResponse();
                    break;
                case 3:
                    viewRequests();
                    break;
                case 4:
                    viewResponses();
                    break;
                case 5:
                    viewChefNotifications();
                    break;
                case 6:
                    System.out.println("Thank you for using Food Request Forum.");
                    System.exit(0);
            }
        }
    }

    private static void viewChefNotifications() {
        System.out.println("\nChef1 (indian, vegetarian, spicy) Notifications:");
        requestService.getChefNotifications("chef1").forEach(System.out::println);
        
        System.out.println("\nChef2 (chinese, seafood) Notifications:");
        requestService.getChefNotifications("chef2").forEach(System.out::println);
        
        System.out.println("\nChef3 (italian, pizza, pasta) Notifications:");
        requestService.getChefNotifications("chef3").forEach(System.out::println);
    }

    private static void createFoodRequest() {
        System.out.println("Enter dish name:");
        String dishName = scanner.nextLine();

        System.out.println("Enter description:");
        String description = scanner.nextLine();

        System.out.println("Enter tags (comma separated):");
        String[] tags = scanner.nextLine().toLowerCase().split(",");

        FoodRequest request = new FoodRequest();
        request.setId(UUID.randomUUID().toString());
        request.setDishName(dishName);
        request.setDescription(description);
        request.setLocation(new Location("Bangalore", "Indiranagar", "12th Main"));
        request.setTags(Arrays.asList(tags));

        requestService.createFoodRequest(request);
    }

    private static void addChefResponse() {
        System.out.println("Enter request ID:");
        String requestId = scanner.nextLine();

        System.out.println("Enter price:");
        double price = scanner.nextDouble();

        ChefResponse response = new ChefResponse();
        response.setId(UUID.randomUUID().toString());
        response.setChefId("chef1");
        response.setRequestId(requestId);
        response.setPrice(new BigDecimal(price));
        response.setPreparationTime(Duration.ofMinutes(30));
        response.setStatus(ResponseStatus.PENDING);

        responseService.createResponse(response);
    }

    private static void viewRequests() {
        List<FoodRequest> requests = requestService.getActiveRequests();
        requests.forEach(System.out::println);
    }

    private static void viewResponses() {
        System.out.println("Enter request ID:");
        String requestId = scanner.nextLine();
        List<ChefResponse> responses = responseService.getResponsesForRequest(requestId);
        responses.forEach(System.out::println);
    }
}