package com.texoit.airton.movieapi;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.texoit.airton.movieapi.controller.ProducerController;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ProducerControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private ProducerController producerController;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.producerController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getProducerIntervalPrizesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/producer/interval-prizes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Verificar se há exatamente 1 resultado min (Joel Silver)
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.min.length()").value(1))
                .andExpect(jsonPath("$.min[0].producer").value("Joel Silver"))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(1990))
                .andExpect(jsonPath("$.min[0].followingWin").value(1991))

                // Verificar se há exatamente 1 resultado max (Matthew Vaughn)
                .andExpect(jsonPath("$.max").isArray())
                .andExpect(jsonPath("$.max.length()").value(1))
                .andExpect(jsonPath("$.max[0].producer").value("Matthew Vaughn"))
                .andExpect(jsonPath("$.max[0].interval").value(13))
                .andExpect(jsonPath("$.max[0].previousWin").value(2002))
                .andExpect(jsonPath("$.max[0].followingWin").value(2015));
    }

    // Manter o teste antigo para compatibilidade
    @Test
    public void getGreatestWinnersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/producer/interval-prizes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.min.*.producer", hasItem(is("Joel Silver"))))
                .andExpect(jsonPath("$.max.*.producer", hasItem(is("Matthew Vaughn"))));
    }
}
