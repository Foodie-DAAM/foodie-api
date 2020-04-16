package net.sandrohc.foodie.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.sandrohc.foodie.model.Recipe;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@ToString
public class RecipeSimple {

	@Id
	@EqualsAndHashCode.Include
	private int id;
	private String title;
 	private String picture;
	private String description;

	public RecipeSimple(Recipe recipe) {
		this.id = recipe.getId();
		this.title = recipe.getTitle();
		this.picture = recipe.getPicture();
		this.description = recipe.getDescription();
	}

}
