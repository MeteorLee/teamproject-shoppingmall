package project.finalproject1backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.dto.product.ProductFormDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {


    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @DisplayName("상품등록")
    @Test
    public void Test1() throws Exception {
        ProductFormDto productDto = new ProductFormDto().builder()
                .id(1l)
                .productName("test")
                .productPrice(1000)
                .stockNumber(100)
                .maincategory(MainCategory.BATHROOM_SUPPLIES)
                .build();


        mvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andDo(print());

    }


}