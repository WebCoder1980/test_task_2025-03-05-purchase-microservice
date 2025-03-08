package ru.isands.test.estore.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.isands.test.estore.dao.entity.*;
import ru.isands.test.estore.dao.repo.PurchaseRepository;
import ru.isands.test.estore.dto.PurchaseDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public PurchaseDTO add(PurchaseDTO purchaseDTO) {
        Purchase purchase = mapToEntity(purchaseDTO);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return mapToDTO(savedPurchase);
    }

    public List<PurchaseDTO> getAll(int start, int limit) {
        return purchaseRepository.findAll().stream()
                .sorted(Comparator.comparing(Purchase::getId))
                .skip(start)
                .limit(limit)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PurchaseDTO getById(Long id) {
        return purchaseRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));
    }

    public PurchaseDTO update(Long id, PurchaseDTO purchaseDTO) {
        Purchase existingPurchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));

        updateEntityFromDTO(purchaseDTO, existingPurchase);
        Purchase updatedPurchase = purchaseRepository.save(existingPurchase);
        return mapToDTO(updatedPurchase);
    }

    public void delete(Long id) {
        if (!purchaseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден");
        }
        purchaseRepository.deleteById(id);
    }

    private Purchase mapToEntity(PurchaseDTO purchaseDTO) {
        Purchase purchase = new Purchase();
        updateEntityFromDTO(purchaseDTO, purchase);
        return purchase;
    }

    private void updateEntityFromDTO(PurchaseDTO purchaseDTO, Purchase purchase) {
        ElectroItem electroItem = new ElectroItem();
        electroItem.setId(purchaseDTO.getElectroId());
        purchase.setElectroItem(electroItem);

        Employee employee = new Employee();
        employee.setId(purchaseDTO.getEmployeeId());
        purchase.setEmployee(employee);

        Shop shop = new Shop();
        shop.setId(purchaseDTO.getShopId());
        purchase.setShop(shop);

        purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());

        PurchaseType purchaseType = new PurchaseType();
        purchaseType.setId(purchaseDTO.getType());
        purchase.setType(purchaseType);
    }

    public void processCSVFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("Windows-1251")))) {
            String line;
            List<PurchaseDTO> purchases = new ArrayList<>();

            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(";");

                if (data.length < 6) {
                    throw new RuntimeException("Файл содержит меньше столбцов, чем нужно");
                }

                PurchaseDTO purchaseDTO = new PurchaseDTO();
                purchaseDTO.setElectroId(Long.parseLong(data[1].trim()));
                purchaseDTO.setEmployeeId(Long.parseLong(data[2].trim()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                purchaseDTO.setPurchaseDate(LocalDateTime.parse(data[3].trim(), formatter));

                purchaseDTO.setType(Long.parseLong(data[4].trim()));
                purchaseDTO.setShopId(Long.parseLong(data[5].trim()));

                purchases.add(purchaseDTO);
            }

            saveAllPurchases(purchases);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла", e);
        }
    }

    public void saveAllPurchases(List<PurchaseDTO> purchaseDTOS) {
        List<Purchase> purchase = purchaseDTOS.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        purchase.forEach(i -> purchaseRepository.save(i));
    }

    private PurchaseDTO mapToDTO(Purchase purchase) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setElectroId(purchase.getElectroItem().getId());
        purchaseDTO.setEmployeeId(purchase.getEmployee().getId());
        purchaseDTO.setShopId(purchase.getShop().getId());
        purchaseDTO.setPurchaseDate(purchase.getPurchaseDate());
        purchaseDTO.setType(purchase.getType().getId());

        return purchaseDTO;
    }
}
