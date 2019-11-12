package com.simon.eurder.api.shipment;

import com.simon.eurder.service.order.OrderServiceTestSetUp;
import com.simon.eurder.service.shipment.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "api/v1/shipments")
@RestController
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }


    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String getOverViewOfTodaysShipments() {
        return shipmentService.getShippingOverviewForDay();
    }
}
