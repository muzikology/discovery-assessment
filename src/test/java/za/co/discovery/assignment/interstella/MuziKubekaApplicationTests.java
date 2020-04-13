package za.co.discovery.assignment.interstella;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
import za.co.discovery.assignment.interstella.entity.Edge;
import za.co.discovery.assignment.interstella.entity.Traffic;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.rest.controller.InterstellaController;
import za.co.discovery.assignment.interstella.service.PlanetService;
import za.co.discovery.assignment.interstella.service.ShortestPathService;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
class MuziKubekaApplicationTests {
	@Mock
	View mockView;

	@Test
	void contextLoads() {
	}

	@Mock
	private ShortestPathService shortestPathService;
	private List<Vertex> vertices;
	private List<Edge> edges;
	private List<Traffic> traffics;
	private MockMvc mockMvc;

	@Mock
	private PlanetService planetService;

	@InjectMocks
	private InterstellaController interstellaController;

	@Before
	public void setUp() throws Exception {
		Vertex vertex1 = new Vertex("A", "Earth");
		Vertex vertex2 = new Vertex("B", "Moon");
		Vertex vertex3 = new Vertex("C", "Jupiter");
		Vertex vertex4 = new Vertex("D", "Venus");


		vertices = new ArrayList<>();
		vertices.add(vertex1);
		vertices.add(vertex2);
		vertices.add(vertex3);
		vertices.add(vertex4);

		Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
		Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
		Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
		Edge edge4 = new Edge(4, "4", "B", "H", 2.44f);

		edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);


		Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
		Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
		Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);
		Traffic traffic4 = new Traffic("4", "B", "H", 0.20f);


		traffics = new ArrayList<>();
		traffics.add(traffic1);
		traffics.add(traffic2);
		traffics.add(traffic3);
		traffics.add(traffic4);

		MockitoAnnotations.initMocks(this);
		mockMvc = standaloneSetup(interstellaController)
				.setSingleView(mockView)
				.build();

	}

	@Test
	public void verifyThatSaveExistingVertexViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.vertexExist("A")).thenReturn(true);
		when(planetService.getVertexById("A")).thenReturn(expectedVertex);
		String message = "Planet A already exists as Earth";
		//Verify
		mockMvc.perform(post("/vertex").param("vertexId", "A").param("name", "Earth"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("validationMessage", sameBeanAs(message)))
				.andExpect(view().name("validation"));
	}

	@Test
	public void verifyThatEditVertexViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.getVertexById("vertexId")).thenReturn(expectedVertex);
		//Verify
		mockMvc.perform(get("/vertex/edit/vertexId"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("vertex", sameBeanAs(expectedVertex)))
				.andExpect(view().name("vertexupdate"));
	}

	@Test
	public void verifyThatUpdateVertexViewIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.updateVertex(expectedVertex)).thenReturn(expectedVertex);
		//Verify
		mockMvc.perform(post("/vertexupdate").param("vertexId", "A").param("name", "Earth"))
				.andExpect(status().isOk())
				.andExpect(view().name("redirect:/vertex/" + expectedVertex.getVertexId()));
	}

	@Test
	public void verifyThatDeleteVertexViewIsCorrect() throws Exception {
		//Set
		when(planetService.deleteVertex("vertexId")).thenReturn(true);
		//Verify
		mockMvc.perform(post("/vertex/delete/A"))
				.andExpect(status().isOk())
				.andExpect(view().name("redirect:/vertices"));
	}


}
