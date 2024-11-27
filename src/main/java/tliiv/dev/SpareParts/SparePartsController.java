package tliiv.dev.SpareParts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spare-parts")
public class SparePartsController {

    private final SparePartsService sparePartsService;


    @Autowired
    public SparePartsController(SparePartsService sparePartsService) {
        this.sparePartsService = sparePartsService;
    }


    //spare-parts?page=0&size=10&productName=mut
    //spare-parts?page=0&size=10&seriesNumber=999999
    @GetMapping()
    public List<SpareParts> getAllSpareParts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String seriesNumber) {
        // If name or serialNumber is provided, filter the list
        if (productName != null || seriesNumber != null) {
            return sparePartsService.getPaginatedAndFilteredSpareParts(page, size, productName,seriesNumber);
        } else {
            // Otherwise return all spare parts (paginated)
            return sparePartsService.getPaginatedSpareParts(page, size);
        }
    }

}

