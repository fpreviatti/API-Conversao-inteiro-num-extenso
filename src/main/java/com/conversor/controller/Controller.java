package com.conversor.controller;

import com.conversor.conversao.CurrencyWriter;


import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@org.springframework.stereotype.Controller
public class Controller {
    JSONObject obj = new JSONObject();
    BigDecimal aux;

// produces = MediaType.APPLICATION_JSON_VALUE garante que será "produzido" um arquivo JSON
    @GetMapping(value = "/{chave}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JSONObject getNumeroExtenso(@PathVariable String chave) {

       try {
           int valor = Integer.parseInt(chave);

           if (valor <= 99999 && valor >= -99999) {

               if (valor<0){

                   //Como a classe CurrencyWritter não possui valores negativos, será tratado e inserido dentro desta condição
                   valor = valor*-1;

                   //aux receberá o número convertido
                   aux = new BigDecimal(valor);

                   //insere menos caso seja número negativo e também elimina o último caracter da String retornada
                   obj.put("extenso", "menos "+CurrencyWriter.getInstance().write(aux).substring(0,CurrencyWriter.getInstance().write(aux).length()-1));
                   return obj;

               }

               else if (valor==0){

                   //retorna o objeto Json com valor zero
                   obj.put("extenso", "zero");
                   return obj;

               }

               //Caso não seja número negativo ou 0 retorna o objeto Json com o valor e eliminando o último caracter do retorno da String
               aux = new BigDecimal(chave);
               obj.put("extenso", CurrencyWriter.getInstance().write(aux).substring(0,CurrencyWriter.getInstance().write(aux).length()-1));
               return obj;
           }
       }
       catch(NumberFormatException nfe){
           obj.put("extenso", "valor invalido");
           return obj;
       }

        obj.put("extenso", "valor fora do intervalo [-99999, 99999]");
        return obj;

    }

}
