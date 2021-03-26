package pisibg.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;


public class Product {
    private int id;
    private String name;
    private String description;
    private Manufacturer manufacturer;
    private Subcategory subcategory;
    private double price;
    private Discount discount;
    private ArrayList<Image> images;
}
