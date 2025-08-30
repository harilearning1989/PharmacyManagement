
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.services.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
@Tag(name = "Medicines", description = "Operations to manage medicines")
@Slf4j
public class MedicineRestController {

    private final MedicineService medicineService;

    @Operation(
            summary = "Bulk import medicines",
            description = "Upload a JSON file containing a list of medicines . " +
                    "The API parses the file and saves all records into the database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicines imported successfully",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Inserted 1000 medicines successfully"))),
            @ApiResponse(responseCode = "400", description = "Invalid input or parsing error",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Failed to import medicines: invalid JSON structure"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Upload JSON file with medicines. " +
                    "The JSON must contain a root node `medical_store_inventory` holding an array of medicine records.",
            required = true,
            content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> bulkCreate(@RequestParam("file") MultipartFile file) {
        log.info("bulk Create file {}", file.getOriginalFilename());
        try {
            int size = medicineService.saveMedicines(file);
            log.info("bulk Create file {} size {}", file.getOriginalFilename(), size);
            return ResponseEntity.status(HttpStatus.CREATED).body("Inserted " + size + " medicines successfully");
        } catch (Exception e) {
            log.error("Failed to upload medicine file {}", file.getOriginalFilename(), e);
            return ResponseEntity.badRequest().body("Failed to import medicines: " + e.getMessage());
        }
    }

    @Operation(
            summary = "List all medicines",
            description = "Retrieve all medicines available in the medical store inventory."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of medicines retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MedicineDto.class)))),
            @ApiResponse(responseCode = "404", description = "No medicines found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No medicines available in inventory"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAll() {
        log.info("Fetching all medicines");
        try {
            List<MedicineDto> medicines = medicineService.getAllMedicines();
            if (medicines == null || medicines.isEmpty()) {
                log.warn("No medicines found in inventory");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicines available in inventory");
            }
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            log.error("Failed to fetch medicines", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch medicines: " + e.getMessage());
        }
    }


    @Operation(summary = "Create medicine", description = "Create a new medicine record.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Medicine created successfully",
                            content = @Content(schema = @Schema(implementation = MedicineDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    public ResponseEntity<MedicineDto> create(@Valid @RequestBody MedicineDto dto) {
        log.info("POST /api/v1/medicines - create {}", dto.itemId());
        var created = medicineService.create(dto);
        log.debug("Created medicine {}", created.itemId());
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get medicine by itemId", description = "Fetch a medicine by its item id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Medicine found",
                            content = @Content(schema = @Schema(implementation = MedicineDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{itemId}")
    public ResponseEntity<MedicineDto> getByItemId(@PathVariable String itemId) {
        log.info("GET /api/v1/medicines/{} - getByItemId", itemId);
        return ResponseEntity.ok(medicineService.findByItemId(itemId));
    }


}
