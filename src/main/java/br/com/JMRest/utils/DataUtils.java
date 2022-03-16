package br.com.JMRest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {

	public static String getDataDiferencaDias(Integer qtd) {
		//pega a data atual e adiciona no dia do mês a quantidade de dias no parametro
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, qtd);
		return getDataFormatada(cal.getTime());	
	}
	
	private static String getDataFormatada(Date data) {
		//formatar em string
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}
}
