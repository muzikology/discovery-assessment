package za.co.discovery.assignment.interstella;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.interstella.entity.Edge;
import za.co.discovery.assignment.interstella.entity.Traffic;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.helper.Graph;
import za.co.discovery.assignment.interstella.model.ShortestPathModel;
import za.co.discovery.assignment.interstella.repository.PlanetRepository;
import za.co.discovery.assignment.interstella.service.PlanetService;
import za.co.discovery.assignment.interstella.rest.controller.InterstellaController;
import za.co.discovery.assignment.interstella.service.ShortestPathService;


import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
public class MuziKubekaApplicationTests {
	@Mock
	View mockView;

	@Test
	public void contextLoads() {
	}

	@Mock
	private ShortestPathService shortestPathService;
	private List<Vertex> vertices;
	private List<Edge> edges;
	private List<Traffic> traffics;
	private MockMvc mockMvc;

	@Mock
	private PlanetService planetService;
	@Mock
	ShortestPathModel shortestPathModel;

	@Mock
	PlanetRepository planetRepository;

	@InjectMocks
	public InterstellaController controller;

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
		mockMvc = standaloneSetup(controller)
				.setSingleView(mockView)
				.build();

	}

	@Test
	public void verifyThatListVerticesViewAndModelIsCorrect() throws Exception {
		//Set
		when(planetService.getAllVertices()).thenReturn(vertices);
		setUpFixture();
		//Verify
		mockMvc.perform(get("/interstella/verticess"))
				.andExpect(model().attribute("vertices", sameBeanAs(vertices)))
				.andExpect(view().name("vertices"));
	}

	@Test
	public void verifyThatShowVertexViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.getPlanetById("vertexId")).thenReturn(expectedVertex);
		//Verify
		mockMvc.perform(get("/interstella/vertex/vertexId"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("vertex", sameBeanAs(expectedVertex)))
				.andExpect(view().name("vertexshow"));
	}

	@Test
	public void verifyThatAddVertexViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex();
		//Verify
		mockMvc.perform(get("/interstella/vertex/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("vertex", sameBeanAs(expectedVertex)))
				.andExpect(view().name("vertexadd"));
	}

	@Test
	public void verifyThatSaveVertexViewIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.vertexExist("A")).thenReturn(false);
		when(planetService.saveVertex(expectedVertex)).thenReturn(expectedVertex);

		//Test
		mockMvc.perform(post("/interstella/vertex").param("vertexId", "A").param("name", "Earth"))
				.andExpect(status().isOk())
				.andExpect(view().name("redirect:/vertex/" + expectedVertex.getVertexId()));

		//Verify
		ArgumentCaptor<Vertex> formObjectArgument = ArgumentCaptor.forClass(Vertex.class);
		verify(planetService, times(1)).saveVertex(formObjectArgument.capture());

		Vertex formObject = formObjectArgument.getValue();
		assertThat(formObjectArgument.getValue(), is(sameBeanAs(expectedVertex)));

		assertThat(formObject.getVertexId(), is("A"));
		assertThat(formObject.getName(), is("Earth"));
	}

	@Test
	public void verifyThatSaveExistingVertexViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.vertexExist("A")).thenReturn(true);
		when(planetService.getPlanetById("A")).thenReturn(expectedVertex);
		String message = "Planet A already exists as Earth";
		//Verify
		mockMvc.perform(post("/interstella/vertex").param("vertexId", "A").param("name", "Earth"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("validationMessage", sameBeanAs(message)))
				.andExpect(view().name("validation"));
	}

	@Test
	public void verifyThatEditVertexViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedVertex = new Vertex("A", "Earth");
		when(planetService.getPlanetById("vertexId")).thenReturn(expectedVertex);
		//Verify
		mockMvc.perform(get("/interstella/vertex/edit/vertexId"))
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
		mockMvc.perform(post("/interstella/vertexupdate").param("vertexId", "A").param("name", "Earth"))
				.andExpect(status().isOk())
				.andExpect(view().name("redirect:/vertex/" + expectedVertex.getVertexId()));
	}

	@Test
	public void verifyThatDeleteVertexViewIsCorrect() throws Exception {
		//Set
		when(planetService.deleteVertex("vertexId")).thenReturn(true);
		//Verify
		mockMvc.perform(post("/interstella/vertex/delete/A"))
				.andExpect(status().isOk())
				.andExpect(view().name("redirect:/vertices"));
	}


	@Test
	public void verifyThatShortestPathViewAndModelIsCorrect() throws Exception {
		//Set
		Vertex expectedSource = vertices.get(0);
		when(planetService.getAllVertices()).thenReturn(vertices);
		ShortestPathModel sh = new ShortestPathModel();
		sh.setVertexName(expectedSource.getName());
		//Verify
		mockMvc.perform(get("/interstella/shortest"))
				.andExpect(model().attribute("shortest", sameBeanAs(sh)))
				.andExpect(model().attribute("pathList", sameBeanAs(vertices)))
				.andExpect(view().name("shortest"));
	}


	public void setUpFixture() {
		mockMvc = standaloneSetup(
				new InterstellaController(shortestPathService,planetService, planetRepository)
		)
				.setViewResolvers(getInternalResourceViewResolver())
				.build();
	}

	private InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

}