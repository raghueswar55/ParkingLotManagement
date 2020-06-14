package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

import static parkingService.ParkingType.HANDICAPPED;
import static parkingService.ParkingType.NORMAL;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;
import static vehicles.VehicleSize.MEDIUM;
import static vehicles.VehicleSize.SMALL;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingLot {

    @Mock
    Owner owner;

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstPlace() {
        ParkingLot parkingLot = new ParkingLot("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.parkingSpots[0].vehicle, vehicle);
    }

    @Test
    public void givenVehicleReference_afterParking_parkingAreaStatusShouldBeUpdated() {
        ParkingLot parkingLot = new ParkingLot("parking-1", 1, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.parkingSpots[0].vehicle, vehicle);
        Assert.assertEquals(parkingLot.status, FILLED);
    }

    @Test
    public void givenVehicleReference_afterUnParking_parkingAreaStatusShouldBeUpdated() {
        ParkingLot parkingLot = new ParkingLot("parking-1", 1, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.parkingSpots[0].vehicle, vehicle);
        parkingLot.parkingSpots[0].unPark();
        Assert.assertEquals(parkingLot.status, AVAILABLE);
    }

    @Test
    public void givenVehicleReferenceAfterParking_getParkingSpot_ShouldReturnCorrectSpot() {
        ParkingLot parkingLot = new ParkingLot("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.parkingSpots[0], parkingLot.getParkingSpot(vehicle)[0]);
    }

    @Test
    public void givenVehicleReferenceAndParkingType_thenPark_shouldParkVehicleAtLastPlace() {
        ParkingLot parkingLot = new ParkingLot("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingLot.park(vehicle, HANDICAPPED);
        Assert.assertEquals(parkingLot.parkingSpots[9].vehicle, vehicle);
    }

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstTwoPlace() {
        ParkingLot parkingLot = new ParkingLot("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = MEDIUM;
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.parkingSpots[0].vehicle, vehicle);
        Assert.assertEquals(parkingLot.parkingSpots[1].vehicle, vehicle);
    }

}
