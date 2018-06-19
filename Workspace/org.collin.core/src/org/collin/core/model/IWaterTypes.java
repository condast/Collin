package org.collin.core.model;

import org.condast.commons.strings.StringStyler;

public interface IWaterTypes {

	enum TypeOfWater{
		UNKNOWN(0),
		TRENCH(1),
		CANAL(2),
		TOWN_MOAT(3),
		BROOK(4),
		RIVER(5),
		POOL(6),
		FEN(7),
		LAKE(8),
		GARDEN_POND(9),
		CITY_POND(10);
		
		private int index;
		
		private TypeOfWater( int index ) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static TypeOfWater getTypeOfWater( int index ) {
			for( TypeOfWater wt: values() ) {
				if( wt.index == index )
					return wt;
			}
			return TypeOfWater.UNKNOWN;
		}
	}

	enum WaterFlow{
		UNKNOWN(0),
		RUNNING(1),
		STILL(2);
		
		private int index;
		
		private WaterFlow( int index ) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static WaterFlow getWaterFlow( int index ) {
			for( WaterFlow wt: values() ) {
				if( wt.index == index )
					return wt;
			}
			return WaterFlow.UNKNOWN;
		}
	}

	enum Shapes{
		UNKNOWN(0),
		LINE(1),
		OVAL(2);
		
		private int index;
		
		private Shapes( int index ) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static Shapes getShape( int index ) {
			for( Shapes wt: values() ) {
				if( wt.index == index )
					return wt;
			}
			return Shapes.UNKNOWN;
		}
	}

	long getId();

	String getName();

	String getProperty();

	TypeOfWater getWaterType();

	WaterFlow getWaterFlow();

	Shapes getShape();

}