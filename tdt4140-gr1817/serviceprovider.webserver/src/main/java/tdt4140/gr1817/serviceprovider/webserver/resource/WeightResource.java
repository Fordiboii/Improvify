package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.WeightValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("weight")
public class WeightResource {

    private final Gson gson;
    private final WeightRepository repository;
    private final WeightValidator validator;

    @Inject
    public WeightResource(WeightRepository repository, Gson gson, WeightValidator validator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWeight(String json) {
        Weight weight = gson.fromJson(json, Weight.class);
        if (validator.validate(json)) {
            repository.add(weight);
            return "Weight added";
        } else {
            return "Failed to add weight";
        }
    }
}