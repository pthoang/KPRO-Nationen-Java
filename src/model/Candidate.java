package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Candidate extends Person {

	private static final Set<String> GENDERS_IF_PERSON = ImmutableSet.of("F", "M");
	private SimpleStringProperty municipality = new SimpleStringProperty("");
	private SimpleStringProperty description = new SimpleStringProperty("");
	private SimpleIntegerProperty rank;
	private SimpleIntegerProperty previousYearRank;
	private String status;
	private String gender = "";

	private ArrayList organizations;

	// PG stands for ProductionGrants
	private SimpleIntegerProperty animalsPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty hiredHelpPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty farmingPG = new SimpleIntegerProperty(0);

	private ObservableList<Connection> connections =  FXCollections.observableArrayList();

	public Candidate(String name, int rank, int previousYearRank) {
		super(name, null);
		//this.name = new SimpleStringProperty(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.status = "";
	}


	public Candidate(String name, String imageURL, String despcription, int rank) {
		super(name, imageURL);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.description.setValue(despcription);
	}

	public String getMunicipality() {
		return municipality.get();
	}

	public void setMunicipality(SimpleStringProperty municipality) {
		this.municipality = municipality;
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(SimpleStringProperty description) {
		this.description = description;
	}

	public int getRank() {
		return rank.get();
	}

	public void setRank(SimpleIntegerProperty rank) {
		this.rank = rank;
	}

	public int getPreviousYearRank() {
		return previousYearRank.get();
	}

	public void setPreviousYearRank(SimpleIntegerProperty previousYearRank) {
		this.previousYearRank = previousYearRank;
	}

	public int getAnimalsPG() {
		return animalsPG.get();
	}

	public void setAnimalsPG(SimpleIntegerProperty animalsPG) {
		this.animalsPG = animalsPG;
	}

	public int getHiredHelpPG() {
		return hiredHelpPG.get();
	}

	public void setHiredHelpPG(SimpleIntegerProperty hiredHelpPG) {
		this.hiredHelpPG = hiredHelpPG;
	}

	public int getFarmingPG() {
		return farmingPG.get();
	}

	public void setFarmingPG(SimpleIntegerProperty farmingPG) {
		this.farmingPG = farmingPG;
	}

	public void addConnection(Person person, String description) {
		Connection newConnection = new Connection(this, person, description);
		connections.add(newConnection);
	}

	public ObservableList<Connection> getConnections() {
		return connections;
	}
	
	public void deleteConnection(Connection connection) {
		connections.remove(connection);
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public boolean getIsPerson(){ 
		if (GENDERS_IF_PERSON.contains(gender)) {
			return true;
		}
		return false;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGender() {
		return gender;
	}

	// Validation
	public String validate(String name, String rank, String previousYearRank, String gender, String description) {
		String errorMessage = "";
		errorMessage += super.validateName(name);

		errorMessage += validateRank(rank);
		errorMessage += validatePreviousYearRank(previousYearRank);
		errorMessage += validateGender(gender);
		errorMessage += validateDescription(description);

		return errorMessage;
	}

	private String validateRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				return "\n Plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			return "\n Plasseringen er ikke et tall";
		}
		return "";
	}

	private String validatePreviousYearRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				return "\n Fjorårets plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			return "\n Fjorårets plasseringen er ikke et tall";
		}
		return "";
	}

	private String validateGender(String gender) {
		if (gender.equals("")) {
			return "\n Du må velge et kjønn. Velg 'Annet' om kandidaten ikke er et menneske.";
		}
		return "";
	}

	private String validateDescription(String description) {
		if (description.length() <= 5 || description.equals(null)) {
			return "\n Beskrivelse mangler:";
		}
		return "";
	}
}
