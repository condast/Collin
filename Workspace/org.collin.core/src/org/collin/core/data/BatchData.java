package org.collin.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import org.collin.core.model.IBatch;
import org.collin.core.model.ILocaleDescription;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.model.IMorphoCode;
import org.collin.core.model.IWaterAnimal;
import org.collin.core.model.IBatch.PlantGrowth;
import org.condast.commons.Utils;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class BatchData {

	private long measurementId;
	private long userId;
	private String userName;
	private Date createDate;
	private Date changeDate;
	private Date measurementDate;
	private double secchi_diepte;//cm
	private double dikte_sliblaag;//cm
	private boolean plants_on_surface;
	private boolean plants_in_water;
	private String description;
	private IMeasurementLocation location;
	private double quality;
	
	private Collection<MeasurementData> measurements;
	
	private Sum sum;

	private Animal[] animals;

	public BatchData( IBatch batch ) {
		this.measurementId = batch.getId();
		this.location = batch.getLocation();
		this.userId = batch.getUserId();
		this.userName = batch.getUserName();
		this.createDate = batch.getCreateDate();
		this.changeDate = batch.getChangeDate();
		this.measurementDate = batch.getMeasurementDate();
		this.secchi_diepte = batch.getSecchiDepth();
		this.dikte_sliblaag = batch.getMudThickness();
		this.plants_on_surface = PlantGrowth.onWater( batch.getPlantGrowth() ); 
		this.plants_in_water = PlantGrowth.underWater( batch.getPlantGrowth() ); 
		this.description = batch.getDescription();
		this.measurements = new ArrayList<MeasurementData>();
		this.quality = batch.getLocation().getWaterQualityIndex();
		
		Collection<Animal> temp = new ArrayList<Animal>();
		Collection<IMorphoCode> species = new TreeSet<IMorphoCode>();
		int total = 0;
		for( IMeasurement measurement: batch.getMeasurements() ) {
			if(( measurement == null ) || ( measurement.getWaterAnimal() == null ) ||
					(  measurement.getWaterAnimal().getMorphologicalCode() == null ))
				continue;
			measurements.add( new MeasurementData( measurement ));
			temp.add( new Animal( measurement.getWaterAnimal(), measurement.getAmount() ));
			total += measurement.getAmount();
			species.add( measurement.getWaterAnimal().getMorphologicalCode());
		}
		animals = temp.toArray( new Animal[temp.size()]);
		this.sum = new Sum( total, species.size() );
	}

	public long getUserId() {
		return userId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public IMeasurementLocation getLocation() {
		return location;
	}


	public MeasurementData[] getMeasurements() {
		return this.measurements.toArray( new MeasurementData[ this.measurements.size() ]);
	}
	
	public double getAverageQuality() {
		if( Utils.assertNull(animals ))
			return 0;
		double total = 0;
		for( Animal animal: animals ) {
			total += animal.quality;
		}
		return total/animals.length;	
	}

	public int getAnimals() {
		return Utils.assertNull(animals )?0: animals.length;
	}
	
	public int getTotalSpecies() {
		return this.sum.species;
	}

	public int getTotalAnimals() {
		return this.sum.animals;
	}

	private class Sum{

		int animals;
		int species;
		public Sum(int animals, int species) {
			super();
			this.animals = animals;
			this.species = species;
		}		
	}

	private class Animal{
		long animalId;
		String latinName;
		String name;
		int amount;
		int quality;
		
		public Animal( IWaterAnimal animal, int amount) {
			super();
			this.animalId = animal.getId();
			this.amount = amount;
			this.quality = animal.getQualityIndicator();
			if( animal.getMorphologicalCode() == null ) {
				this.latinName = "unknown";
				this.name = "onbekend";
				return;
			}
			this.latinName = animal.getMorphologicalCode().getName();
			this.name = animal.getMorphologicalCode().getDescription( ILocaleDescription.DefaultDescriptions.NL.name()).getName();
		}
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson( this, BatchData.class );
	}
	
	public static Collection<BatchData> create( Collection<? extends IBatch> batches ){
		Collection<BatchData> results = new ArrayList<>();
		for( IBatch batch: batches )
			results.add( new BatchData(batch));
		return results;
	}

}
