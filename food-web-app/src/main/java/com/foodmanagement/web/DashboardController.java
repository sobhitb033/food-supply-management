package com.foodmanagement.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final RestClient nutritionClient;
    private final RestClient supplyClient;

    // configure REST clients for both backend services
    public DashboardController(
            @org.springframework.beans.factory.annotation.Value("${app.nutrition-service.url}") String nutritionUrl,
            @org.springframework.beans.factory.annotation.Value("${app.supply-service.url}") String supplyUrl) {

        this.nutritionClient = RestClient.builder()
                .baseUrl(nutritionUrl)
                .build();

        this.supplyClient = RestClient.builder()
                .baseUrl(supplyUrl)
                .build();
    }

    // display dashboard with live backend data
    @GetMapping("/")
    public String dashboard(Model model) {

        // fetch data from the nutrition service
        List<?> beneficiaries = getList(
                nutritionClient, "/api/beneficiaries");

        List<?> foodItems = getList(
                nutritionClient, "/api/food-items");

        List<?> programmes = getList(
                nutritionClient, "/api/programmes");

        // fetch data from the supply service
        List<?> suppliers = getList(
                supplyClient, "/api/suppliers");

        List<?> inventory = getList(
                supplyClient, "/api/inventory");

        List<?> procurement = getList(
                supplyClient, "/api/procurement");

        List<?> distributions = getList(
                supplyClient, "/api/distributions");

        // fetch inventory alerts from the supply service
        List<?> lowStock = getList(
                supplyClient, "/api/inventory/low-stock");

        List<?> expiring = getList(
                supplyClient, "/api/inventory/expiring?days=30");

        // pass alerts to the dashboard template
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("expiring", expiring);

        // add dashboard numbers and lists to the page
        model.addAttribute("beneficiaryCount", beneficiaries.size());
        model.addAttribute("foodItemCount", foodItems.size());
        model.addAttribute("programmeCount", programmes.size());
        model.addAttribute("supplierCount", suppliers.size());
        model.addAttribute("inventoryCount", inventory.size());
        model.addAttribute("procurementCount", procurement.size());
        model.addAttribute("distributionCount", distributions.size());

        model.addAttribute("beneficiaries", beneficiaries);
        model.addAttribute("inventory", inventory);
        model.addAttribute("procurement", procurement);
        model.addAttribute("distributions", distributions);

        // calculate total stock across all inventory batches
        double totalStock = inventory.stream()
                .mapToDouble(batch -> {
                    if (batch instanceof java.util.Map<?, ?> map
                            && map.get("quantity") instanceof Number quantity) {
                        return quantity.doubleValue();
                    }
                    return 0.0;
                })
                .sum();

        // count procurement requests that are still pending
        long pendingProcurement = procurement.stream()
                .filter(request -> request instanceof java.util.Map<?, ?> map
                        && "PENDING".equalsIgnoreCase(
                                String.valueOf(map.get("status"))))
                .count();

        // count distributions that have been delivered
        long completedDistributions = distributions.stream()
                .filter(distribution -> distribution instanceof java.util.Map<?, ?> map
                        && "DELIVERED".equalsIgnoreCase(
                                String.valueOf(map.get("status"))))
                .count();

        // pass analytics to the dashboard page
        model.addAttribute("totalStock", totalStock);
        model.addAttribute("pendingProcurement", pendingProcurement);
        model.addAttribute("completedDistributions", completedDistributions);

        return "dashboard";
    }

    // return an empty list if a backend service is temporarily unavailable
    private List<?> getList(RestClient client, String endpoint) {
        try {
            List<?> result = client.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(List.class);

            return result == null ? List.of() : result;
        } catch (Exception exception) {
            return List.of();
        }
    }
}