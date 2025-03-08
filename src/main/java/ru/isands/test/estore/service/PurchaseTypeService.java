package ru.isands.test.estore.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.isands.test.estore.dao.entity.*;
import ru.isands.test.estore.dao.repo.PurchaseTypeRepository;
import ru.isands.test.estore.dto.PurchaseTypeDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseTypeService {

    private final PurchaseTypeRepository purchaseTypeRepository;

    public PurchaseTypeService(PurchaseTypeRepository purchaseTypeRepository) {
        this.purchaseTypeRepository = purchaseTypeRepository;
    }

    public PurchaseTypeDTO add(PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType purchaseType = mapToEntity(purchaseTypeDTO);
        PurchaseType savedPurchase = purchaseTypeRepository.save(purchaseType);
        return mapToDTO(savedPurchase);
    }

    public List<PurchaseTypeDTO> getAll(int start, int limit) {
        return purchaseTypeRepository.findAll().stream()
                .sorted(Comparator.comparing(PurchaseType::getId))
                .skip(start)
                .limit(limit)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PurchaseTypeDTO getById(Long id) {
        return purchaseTypeRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));
    }

    public PurchaseTypeDTO update(Long id, PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType existingPurchase = purchaseTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));

        updateEntityFromDTO(purchaseTypeDTO, existingPurchase);
        PurchaseType updatedPurchase = purchaseTypeRepository.save(existingPurchase);
        return mapToDTO(updatedPurchase);
    }

    public void delete(Long id) {
        if (!purchaseTypeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден");
        }
        purchaseTypeRepository.deleteById(id);
    }

    private PurchaseType mapToEntity(PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType purchaseType = new PurchaseType();
        updateEntityFromDTO(purchaseTypeDTO, purchaseType);
        return purchaseType;
    }

    private void updateEntityFromDTO(PurchaseTypeDTO purchaseTypeDTO, PurchaseType purchaseType) {
        purchaseType.setName(purchaseTypeDTO.getName());
    }

    public void processCSVFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("Windows-1251")))) {
            String line;
            List<PurchaseTypeDTO> purchases = new ArrayList<>();

            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(";");

                if (data.length < 2) {
                    throw new RuntimeException("Файл содержит меньше столбцов, чем нужно");
                }

                PurchaseTypeDTO purchaseTypeDTO = new PurchaseTypeDTO();
                purchaseTypeDTO.setName(data[1].trim());

                purchases.add(purchaseTypeDTO);
            }

            saveAllPurchases(purchases);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла", e);
        }
    }

    public void saveAllPurchases(List<PurchaseTypeDTO> purchaseDTOS) {
        List<PurchaseType> purchaseTypes = purchaseDTOS.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        purchaseTypes.forEach(i -> purchaseTypeRepository.save(i));
    }

    private PurchaseTypeDTO mapToDTO(PurchaseType purchaseType) {
        PurchaseTypeDTO purchaseTypeDTO = new PurchaseTypeDTO();
        purchaseTypeDTO.setId(purchaseType.getId());
        purchaseTypeDTO.setName(purchaseType.getName());

        return purchaseTypeDTO;
    }
}
