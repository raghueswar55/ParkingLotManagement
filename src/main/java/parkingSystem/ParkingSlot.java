package parkingSystem;

import vehicles.Vehicle;
import vehicles.VehicleSize;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static parkingSystem.ParkingType.HANDICAPPED;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

public class ParkingSlot {
    public char name;
    public Attendant parkingAttendant;
    public ParkingLot parkingLot;
    public ParkingSpot[] parkingSpots;
    public Status status;
    public int emptySpots;

    public ParkingSlot(char name, int numberOfSpotsPerSlot, ParkingLot parkingLot, Attendant parkingAttendant) {
        this.name = name;
        this.parkingLot = parkingLot;
        this.parkingAttendant = parkingAttendant;
        this.parkingSpots = createParkingSpots(numberOfSpotsPerSlot);
        this.status = AVAILABLE;
        this.emptySpots = this.parkingSpots.length;
    }

    private ParkingSpot[] createParkingSpots(int numberOfSpotsPerSlot) {
        ParkingSpot[] parkingSpots = new ParkingSpot[numberOfSpotsPerSlot];
        for (int i = 0; i < parkingSpots.length; i++) {
            parkingSpots[i] = new ParkingSpot(i+1,this);
        }
        return parkingSpots;
    }

    public boolean park(Vehicle vehicle, ParkingType type) {
        ParkingSpot[] spots = getParkingSpot(type, vehicle.vehicleSize);
        Arrays.stream(spots).forEach(parkingSpot -> parkingSpot.park(vehicle, type));
        vehicle.parkingSpots = spots;
        this.emptySpots -= spots.length;
        return Arrays.stream(spots).allMatch(parkingSpot -> vehicle.equals(parkingSpot.vehicle));
    }

    public boolean canPark(VehicleSize vehicleSize) {
        int counter = 0;
        if (this.status.equals(FILLED) && this.emptySpots < vehicleSize.size)
            return false;
        for (int i = 0; i < this.parkingSpots.length; i++) {
            if (this.parkingSpots[i].status.equals(AVAILABLE)) {
                counter = 1;
                for (int j = 1; j < vehicleSize.size && i+j < this.parkingSpots.length; j++) {
                    if (this.parkingSpots[i+j].status.equals(AVAILABLE))
                        counter++;
                }
            }
            if (counter == vehicleSize.size)
                break;
        }
        return counter == vehicleSize.size;
    }

    private ParkingSpot[] getParkingSpot(ParkingType type, VehicleSize vehicleSize) {
        List<ParkingSpot> availableParkingSpots = Arrays.stream(this.parkingSpots)
                .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                .collect(Collectors.toList());
        if (type.equals(HANDICAPPED))
            return availableParkingSpots.subList(availableParkingSpots.size()-vehicleSize.size, availableParkingSpots.size())
                    .toArray(ParkingSpot[]::new);
        return availableParkingSpots.subList(0, vehicleSize.size).toArray(ParkingSpot[]::new);
    }

    public void updateStatus() {
        if (this.status != (this.status = Arrays.stream(this.parkingSpots)
                .anyMatch(parkingSlot -> parkingSlot.status.equals(AVAILABLE))? AVAILABLE : FILLED ))
            this.parkingLot.updateStatus();
    }
}
