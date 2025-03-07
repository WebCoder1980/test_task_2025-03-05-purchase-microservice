package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.PurchaseDTO;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.service.PurchaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Purchase", description = "Сервис для выполнения операций над покупками")
@RequestMapping("/estore/api/purchase")
public class PurchaseController {

	private final PurchaseService purchaseService;

	public PurchaseController(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	@PostMapping
	@Operation(summary = "Добавить покупку", responses = {
			@ApiResponse(responseCode = "200", description = "Покупка добавлена"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<PurchaseDTO> add(@RequestBody PurchaseDTO purchaseDTO) {
		return ResponseEntity.ok(purchaseService.add(purchaseDTO));
	}

	@GetMapping
	@Operation(summary = "Получить все покупки", parameters = {
			@Parameter(name = "start", description = "Номер первого в результате покупки", schema = @Schema(type = "integer", defaultValue = "0")),
			@Parameter(name = "limit", description = "Максимальное колличество покупок в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
			}, responses = {
			@ApiResponse(responseCode = "200", description = "Список покупок"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<PurchaseDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
		return ResponseEntity.ok(purchaseService.getAll(start, limit));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить покупки по ID", responses = {
			@ApiResponse(responseCode = "200", description = "Информация о покупках"),
			@ApiResponse(responseCode = "404", description = "Покупки не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
	})
	public ResponseEntity<PurchaseDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(purchaseService.getById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить информацию о покупке", responses = {
			@ApiResponse(responseCode = "200", description = "Покупка обновлена"),
			@ApiResponse(responseCode = "404", description = "Покупка не найдена", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<PurchaseDTO> update(@PathVariable Long id, @RequestBody PurchaseDTO purchaseDTO) {
		return ResponseEntity.ok(purchaseService.update(id, purchaseDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить покупку", responses = {
			@ApiResponse(responseCode = "204", description = "Покупка удалена"),
			@ApiResponse(responseCode = "404", description = "Покупка не найдена", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		purchaseService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/upload-csv")
	@Operation(summary = "Загрузить покупки из CSV", responses = {
			@ApiResponse(responseCode = "200", description = "Покупки успешно загружены"),
			@ApiResponse(responseCode = "404", description = "Покупка не найдена", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
		}

		try {
			purchaseService.processCSVFile(file);
			return ResponseEntity.ok("Данные покупок успешно загружены.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
		}
	}
}
