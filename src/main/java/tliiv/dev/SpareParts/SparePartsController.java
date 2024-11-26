package tliiv.dev.SpareParts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    public List<SpareParts> getAllSpareParts() {
        return sparePartsService.getSparePartsList();
    }
}

