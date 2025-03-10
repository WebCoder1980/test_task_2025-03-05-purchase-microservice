package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.*;
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
			@Parameter(name = "limit", description = "Максимальное колличество покупок в результате", schema = @Schema(type = "integer", defaultValue = "1000000")),
			@Parameter(name = "orderby", description = "Поле, по которому производится сортировка. Доступны id и purchasedate", schema = @Schema(type = "string", defaultValue = "id")),
			@Parameter(name = "sortisreversed", description = "Является ли сортировка в перевёрнутом порядке", schema = @Schema(type = "string", defaultValue = "id"))
			}, responses = {
			@ApiResponse(responseCode = "200", description = "Список покупок"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<PurchaseDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit, @RequestParam(value = "orderby", defaultValue = "id") String orderBy, @RequestParam(value = "sortisreversed", defaultValue = "false") Boolean sortIsReversed) {
		return ResponseEntity.ok(purchaseService.getAll(start, limit, orderBy, sortIsReversed));
	}

	@GetMapping("totalcountbyemployee")
	@Operation(summary = "Получить колличество всех покупок сгруппировав по сотруднику и отсортировав по колличеству в обратном порядке", responses = {
			@ApiResponse(responseCode = "200", description = "Колличество всех покупок сгруппировав по сотруднику и отсортировав по сумме в обратном порядке"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<PurchaseCountByEmployeeDTO>> getTotalCountByEmployee() {
		return ResponseEntity.ok(purchaseService.getTotalCountByEmployee());
	}

	@GetMapping("totalamountbyemployee")
	@Operation(summary = "Получить сумму всех покупок сгруппировав по сотруднику и отсортировав по сумме в обратном порядке", responses = {
			@ApiResponse(responseCode = "200", description = "Колличество всех покупок сгруппировав по сотруднику и отсортировав по сумме в обратном порядке"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<PurchaseAmountByEmployeeDTO>> getTotalAmountByEmployee() {
		return ResponseEntity.ok(purchaseService.getTotalAmountByEmployee());
	}

	@GetMapping("juniorsalesconsultant-smartwatches")
	@Operation(summary = "Получить лучшего младшего продавца-консультанта, продавшего больше всех умных часов", responses = {
			@ApiResponse(responseCode = "200", description = "Вывод лучшего младшего продавца-консультанта, продавшего больше всех умных часов"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<PurchaseJuniorSalesConsultant_smartWatchesDTO> getJuniorSalesConsultant_smartWatches() {
		return ResponseEntity.ok(purchaseService.getJuniorSalesConsultant_smartWatches());
	}

	@GetMapping("purchaseamountbypurchasetype")
	@Operation(summary = "Получить суммы денежных средств, полученной магазином через оплату наличными", responses = {
			@ApiResponse(responseCode = "200", description = "Вывод суммы денежных средств, полученной магазином через оплату наличными"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<PurchaseAmountByPurchaseTypeDTO>> getPurchaseAmountByPurchaseTypeDTO() {
		return ResponseEntity.ok(purchaseService.getPurchaseAmountByPurchaseTypeDTO());
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
