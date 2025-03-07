package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.dto.PurchaseTypeDTO;
import ru.isands.test.estore.service.PurchaseTypeService;

import java.util.List;

@RestController
@Tag(name = "PurchaseType", description = "Сервис для выполнения операций над типами покупок")
@RequestMapping("/estore/api/purchasetype")
public class PurchaseTypeController {

	private final PurchaseTypeService purchaseTypeService;

	public PurchaseTypeController(PurchaseTypeService purchaseTypeService) {
		this.purchaseTypeService = purchaseTypeService;
	}

	@PostMapping
	@Operation(summary = "Добавить тип покупки", responses = {
			@ApiResponse(responseCode = "200", description = "Тип покупки добавлена"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<PurchaseTypeDTO> add(@RequestBody PurchaseTypeDTO purchaseTypeDTO) {
		return ResponseEntity.ok(purchaseTypeService.add(purchaseTypeDTO));
	}

	@GetMapping
	@Operation(summary = "Получить все типы покупок", parameters = {
			@Parameter(name = "start", description = "Номер первого в результате типа покупки", schema = @Schema(type = "integer", defaultValue = "0")),
			@Parameter(name = "limit", description = "Максимальное колличество типов покупок в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
			}, responses = {
			@ApiResponse(responseCode = "200", description = "Список типов покупок"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<PurchaseTypeDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
		return ResponseEntity.ok(purchaseTypeService.getAll(start, limit));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить тип покупки по ID", responses = {
			@ApiResponse(responseCode = "200", description = "Информация о типах покупки"),
			@ApiResponse(responseCode = "404", description = "Тип покупки не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
	})
	public ResponseEntity<PurchaseTypeDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(purchaseTypeService.getById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить информацию о типе покупки", responses = {
			@ApiResponse(responseCode = "200", description = "Тип покупки обновлена"),
			@ApiResponse(responseCode = "404", description = "Тип покупки не найдена", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<PurchaseTypeDTO> update(@PathVariable Long id, @RequestBody PurchaseTypeDTO purchaseTypeDTO) {
		return ResponseEntity.ok(purchaseTypeService.update(id, purchaseTypeDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить покупку", responses = {
			@ApiResponse(responseCode = "204", description = "Тип поркупка удалена"),
			@ApiResponse(responseCode = "404", description = "Тип покупка не найдена", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		purchaseTypeService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/upload-csv")
	@Operation(summary = "Загрузить покупки из CSV", responses = {
			@ApiResponse(responseCode = "200", description = "Тип покупки успешно загружены"),
			@ApiResponse(responseCode = "404", description = "Тип покупки не найдена", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
		}

		try {
			purchaseTypeService.processCSVFile(file);
			return ResponseEntity.ok("Данные о типе покупки успешно загружены.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
		}
	}
}
