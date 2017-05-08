package data.members;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseGeoPoint;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import data.management.DBManager;

/**
 * @author Inbal Matityahu
 * @author David Cohen
 * @author Toma
 * @author dshames]
 * @since 12.11.16 This class represent a parking area inside the Technion
 */

public class ParkingArea extends dbMember {

	private StickersColor color;
	private int areaId;
	private String name;
	private MapLocation location;
	private Set<ParkingSlot> parkingSlots;
	
	private final String objectClass = "ParkingArea";

	/* Constructors */

	// Retrieve an exiting area from DB by the name
	public ParkingArea(final String name) throws ParseException {
		
		DBManager.initialize();
		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		Map<String,Object> fields = DBManager.getObjectFieldsByKey(objectClass, keys);
		this.color = StickersColor.values()[(int)fields.get("color")];
		System.out.println("COOL");
		this.areaId = (int)fields.get("areaId"); 
		this.name = (String)fields.get("name");
		final ParseGeoPoint geo = (ParseGeoPoint) fields.get("location");
		this.location = new MapLocation(geo.getLatitude(), geo.getLongitude());
		// handle warning
		this.parkingSlots = convertToSlots((List<ParseObject>)fields.get("parkingSlots"));
		

	}

	// Create a new parking area
	public ParkingArea(final int areaId, final String name, final MapLocation location, final Set<ParkingSlot> parkingSlots,
			final StickersColor defaultColor) throws ParseException {
		
		// TODO: check if already exists?
		this.areaId = areaId;
		this.name = name;
		this.location = location;
		this.parkingSlots = parkingSlots;
		this.color = defaultColor;
		
		DBManager.initialize();
		
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("color", this.color.ordinal());
		fields.put("areaId", this.areaId);
		fields.put("name", this.name);
		fields.put("location", new ParseGeoPoint(this.location.getLat(), this.location.getLon()));
		fields.put("parkingSlots", parkingSlotsSetToParseList());
		//fields.put("parkingSlots", new ArrayList<ParseObject>());
		

		
		Map<String, Object> keyValues = new HashMap<String, Object>();
		keyValues.put("name", this.name);
		DBManager.insertObject(objectClass, keyValues, fields);
		
	}

	public ParkingArea(final ParseObject parseObject) throws ParseException {
		DBManager.initialize();
		this.parseObject = parseObject;
		areaId = (Integer) parseObject.get("areaId");
		name = (String) parseObject.get("name");
		color = StickersColor.values()[(Integer) parseObject.get("color")];
		final ParseGeoPoint geo = this.parseObject.getParseGeoPoint("location");
		location = new MapLocation(geo.getLatitude(), geo.getLongitude());
		parkingSlots = convertToSlots((List<ParseObject>)parseObject.get("parkingSlots"));
		objectId = parseObject.getObjectId();
		parseObject.save();
	}
	
	// V
	public int getAreaId() {
		return this.areaId;
	}
	// V
	public String getName() {
		System.out.print("Get name is called");
		return this.name;
	}
	// V
	public int getNumOfParkingSlots() {
		if (parkingSlots == null)
			return 0;
		return parkingSlots.size();
	}
	// V
	public MapLocation getLocation() {
		return this.location;
	}
	// V
	public Set<ParkingSlot> getParkingSlots() {
		return this.parkingSlots;
	}
	// V
	public int getNumOfFreeSlots() {
		final Set<ParkingSlot> $ = getSlotsByStatus(ParkingSlotStatus.FREE);
		return $ == null ? 0 : $.size();
	}
	// V
	public Set<ParkingSlot> getSlotsByStatus(final ParkingSlotStatus s) {
		return parkingSlots.stream().filter(λ -> λ.getStatus().equals(s)).collect(Collectors.toSet());
	}
	// V
	public int getNumOfTakenSlots() {
		final Set<ParkingSlot> $ = getSlotsByStatus(ParkingSlotStatus.TAKEN);
		return $ == null ? 0 : $.size();
	}
	// V
	public Set<ParkingSlot> getFreeSlots() throws ParseException {
		return getSlotsByStatus(ParkingSlotStatus.FREE);
	}
	// V
	public Set<ParkingSlot> getTakenSlots() throws ParseException {
		return getSlotsByStatus(ParkingSlotStatus.TAKEN);
	}
	// V
	public StickersColor getColor() {
		return this.color;
	}
	
	// V
	private Map<String,Object> getFieldsMap(){
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("color", this.color.ordinal());
		fields.put("areaId", this.areaId);
		fields.put("name", this.name);
		fields.put("location", new ParseGeoPoint(this.location.getLat(), this.location.getLon()));
		fields.put("parkingSlots", parkingSlotsSetToParseList());
		return fields;
	}

	// V
	public void setAreaId(final int areaId) {
		this.areaId = areaId;
		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		DBManager.update(objectClass, keys, getFieldsMap());	
	}
	// V
	public void setName(final String name) {
		this.name = name;
		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		DBManager.update(objectClass, keys, getFieldsMap());	
	}
	// V
	public void setLocation(final MapLocation location) {
		this.location = location;
		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		DBManager.update(objectClass, keys, getFieldsMap());	
	}
	// V
	public void setParkingSlots(final Set<ParkingSlot> slots) {
		this.parkingSlots = slots;
		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		DBManager.update(objectClass, keys, getFieldsMap());		
	}
	// V
	public void setColor(final StickersColor color) {
		this.color = color;
		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		DBManager.update(objectClass, keys, getFieldsMap());		
	}
	

	/* Methods */
	public void removeParkingAreaFromDB() throws ParseException {
		// TODO : check if the slots have to be deleted too
		//DBManager.deleteObject(objectClass, getFieldsMap());
	}

	public Set<ParkingSlot> convertToSlots(final List<ParseObject> slots) {
		final List<ParkingSlot> $ = new ArrayList<ParkingSlot>();
		if (slots == null)
			return null;
		for (final ParseObject p : slots)
			try {
				$.add(new ParkingSlot(p));
			} catch (final ParseException ¢) {
				System.out.println("could not add the slot");
				¢.printStackTrace();
			}
		return new HashSet<ParkingSlot>($);
	}

	/*
	 * add new parking slot to this area. notice - this new slot will count as
	 * free slot, and therefore increase the amount of free slots in this area,
	 * and the total count of parking
	 */
	
	
	// V
	public void addParkingSlot(final ParkingSlot parkingSlot) throws ParseException {
		// check if already exists? on in other parkingArea
		parkingSlots.add(parkingSlot);
		//DBManager.update(objectClass, getFieldsMap());	
	}

	/*
	 * add new parking slot to this area. notice - this new slot will count as
	 * free slot, and therefore increase the amount of free slots in this area,
	 * and the total count of parking
	 */
	
	
	
	public void removeParkingSlot(final ParkingSlot s) throws ParseException {
		if (parkingSlots == null)
			return;
		
		for (final Iterator<ParkingSlot> ¢ = parkingSlots.iterator(); ¢.hasNext();)
			if (¢.next().objectId.equalsIgnoreCase(s.objectId))
				¢.remove();
		s.removeParkingSlotFromDB();
		updateSlotsArray();
	}

	// Update the slots array in the DB according to the last update
	private void updateSlotsArray() throws ParseException {
		final List<ParseObject> slots = new ArrayList<ParseObject>();
		if (!parkingSlots.isEmpty())
			for (final ParkingSlot ¢ : parkingSlots)
				slots.add(¢.getParseObject());

		parseObject.put("parkingSlots", slots);
		parseObject.save();
		
	}
	
	// V
	private List<ParseObject> parkingSlotsSetToParseList(){
		List<ParseObject> list = new ArrayList<ParseObject>();
		if(!parkingSlots.isEmpty())
			for(final ParkingSlot s: parkingSlots){
				list.add(DBManager.getParseObject(s));
			}
		return list;	
	}
		
}
