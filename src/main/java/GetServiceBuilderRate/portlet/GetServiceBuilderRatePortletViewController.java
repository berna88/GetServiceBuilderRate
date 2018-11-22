package GetServiceBuilderRate.portlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.consistent.models.rate.Brand;
import com.consistent.models.rate.Content;
import com.consistent.models.rate.Contents;
import com.consistent.models.rate.Medialink;
import com.consistent.models.rate.Medialinks;
import com.consistent.models.rate.Multimedia;
import com.consistent.models.rate.Rate;


/**
 * @author liferay
 */
@Controller
@RequestMapping("VIEW")
public class GetServiceBuilderRatePortletViewController {

	@RenderMapping
	public String view(RenderRequest request, RenderResponse response) throws JAXBException {
		
		String[] args = new String[10];
		
		JAXBContext context = JAXBContext.newInstance(Contents.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		String _rate_en = "/Users/bernardohernandez/Downloads/ratesenglsih.xml";
		String _rate_es = "/Users/bernardohernandez/Downloads/ratesSpanish.xml";
		
		List<Rate> rateEngList = new ArrayList<>();
		List<Rate> rateSpaList = new ArrayList<>();
		
		//String _rateTwo = "";
		if(_rate_en!= null && !_rate_en.equals("")){
			Contents contentsEng = (Contents) unmarshaller.unmarshal(new File(_rate_en));
			Contents contentsSpa = (Contents) unmarshaller.unmarshal(new File(_rate_es));
			
			System.out.println("Mi marca: "+ getBrand(contentsEng));
			System.out.println("XML de marca"+ getXmlBrand(contentsSpa, contentsEng));
			
			System.out.println("contentsEng.getContents().size() : "+ contentsEng.getContents().size());
			System.out.println("contentsEng.getContents().get(0).getBrands().size() : " + contentsEng.getContents().get(0).getBrands().get(0).getCode());
			
			
			
			System.out.println("contentsEng.getContents().get(0).getBrands().get(0).getRates().size() : " + contentsEng.getContents().get(0).getBrands().get(0).getRates().size());
			System.out.println("contentsEng.getContents().get(0).getBrands().get(0).getRates().get(0).getRate().size()" + contentsEng.getContents().get(0).getBrands().get(0).getRates().get(0).getRate().size());
			
			for(Rate rate : contentsEng.getContents().get(0).getBrands().get(0).getRates().get(0).getRate()){
				if(rate.getGuid() != null && !rate.getGuid().equals(""))
					rateEngList.add(rate);
			}
			
			
			System.out.println("-----------------------");
			
			System.out.println("contentsSpa.getContents().size() : "+ contentsSpa.getContents().size());
			System.out.println("contentsSpa.getContents().get(0).getBrands().size() : " + contentsSpa.getContents().get(0).getBrands().size());
			System.out.println("contentsSpa.getContents().get(0).getBrands().get(0).getRates().size() : " + contentsSpa.getContents().get(0).getBrands().get(0).getRates().size());
			System.out.println("contentsSpa.getContents().get(0).getBrands().get(0).getRates().get(0).getRate().size()" + contentsSpa.getContents().get(0).getBrands().get(0).getRates().get(0).getRate().size());
				
			int max = 0;
			for(Rate rate : contentsSpa.getContents().get(0).getBrands().get(0).getRates().get(0).getRate()){
				if(rate.getGuid() != null && !rate.getGuid().equals(""))
					rateSpaList.add(rate);
			}
			
			
			for (int i = 0; i < args.length; i++) {
				String string = args[i];
				
			}
			
			System.out.println("English: " + rateEngList.size());
			System.out.println("Spanish : " + rateSpaList.size());
		//	System.out.println(contentsSpa.getContents().get(0).getBrands().get(0).getRates().get(0).getRate().size());
			
			Map<Rate,Rate> mapper = new HashMap<>();
			
			List<Rate> maxList = null;
			List<Rate> minList = null;
				
			if(rateEngList.size() > rateSpaList.size())
			{
				maxList=rateEngList;
				minList=rateSpaList;
			}
			else if(rateSpaList.size() > rateEngList.size())
		{
				maxList=rateSpaList;
				minList=rateEngList;
		}
			else
		{
				maxList = rateEngList;
				minList=rateSpaList;
		}
			int diferencia = 0;
			
			for(Rate rMax: maxList){
				boolean flag = false;	
				for(Rate rMin: minList){
					
					if(rMax.getCode().equals(rMin.getCode()))
					{
						flag = true;
						mapper.put(rMax, rMin);
						minList.remove(rMin);
						max++;
						break;
					}
						
					if(flag==false){
						mapper.put(rMax, new Rate());
					}
				}
			}
			int resto = 0, sinValor = 0, general = 0;
			System.out.println("Tamaño: "+mapper.size() + " Valores iguales: " + max);
			List<Rate> rateOnlyLanguage = new ArrayList<>();
			for(Map.Entry<Rate, Rate> rate12: mapper.entrySet()){
				if(!rate12.getValue().getCode().isEmpty()){
					System.out.println("key: "+rate12.getKey().getCode()+" value: "+rate12.getValue().getCode());
					//getRateUrl(rate12.getValue());
					general++;
				}
				
				if(rate12.getValue().getCode().equals("")){
					sinValor++;
					for(int i = 0; i < mapper.size(); i++){
						if(rate12.getKey().getCode().equals(maxList.get(i).getCode())){
							rateOnlyLanguage.add(maxList.get(i));
							resto++;
							break;
						}
					}
				}
				
				
				//getRate(rate12.getKey(), rate12.getValue());
			}
			System.out.println("resto: "+resto);
			System.out.println("sinValor: "+sinValor);
			System.out.println("General: "+general);
			System.out.println("Objeto con un solo idioma");
			for(Rate rateOnly: rateOnlyLanguage){
				//System.out.println(getRate(rateOnly, rateOnly));
			}
				
			
			}
		
	
		return "view";
	}
	
	public static void getRateUrl(Rate rate){
		if(rate!=null && !rate.equals("")){
			if(!rate.getMedialinks().isEmpty() && !rate.getMedialinks().equals("") && rate.getMedialinks() != null){
				for(Medialinks medialinks: rate.getMedialinks()){
					if(!medialinks.getMedialinks().isEmpty() && !medialinks.getMedialinks().equals("") && medialinks.getMedialinks() != null){
						for(Medialink medialink : medialinks.getMedialinks()){
							if(!medialink.getMultimedia().isEmpty() && !medialink.getMultimedia().equals("") && medialink.getMultimedia() != null){
								for(Multimedia multimedia : medialink.getMultimedia()){
									if(multimedia!=null && !multimedia.equals("")){
									}
								}
							}
						}
					}	
				}
				
			}
			
		}
	}
	
	public static String getBrand(Contents contents){
		String miMarca = "";
		if(contents!=null && !contents.equals("")){
			for(Content content: contents.getContents()){
				if(content!=null && !content.equals("")){
					for(Brand brand:content.getBrands()){
						if (brand!=null && !brand.equals("")) {
						miMarca = brand.getCode();
						}
					}
				}
			}
		}
		return miMarca;
	}
	public static String getRate(Rate rate_es, Rate rate_en){

		MappingString _dynamics = new MappingString();

				

				String rate = _dynamics.DynamicHeader(

							_dynamics.DynamicElement("Rate", "selection_break", "keyword",

										_dynamics.DynamicElement("typeRate", "radio", "keyword", 

												_dynamics.getDynamicContentString("", "")

												)+

										_dynamics.DynamicElement("codeRate", "text", "keyword",

												_dynamics.getDynamicContent(rate_es.getCode(),rate_en.getCode())

												)+

										_dynamics.DynamicElement("nameRate", "text", "keyword", 

												_dynamics.getDynamicContent(rate_es.getName(), rate_en.getName() )

												)+

										_dynamics.DynamicElement("keywordRate", "text", "keyword", 

												_dynamics.getDynamicContent(rate_es.getKeyword(), rate_en.getKeyword())

												)+

										_dynamics.DynamicElement("descriptionRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("descriptionLongRate", "text_area", "text",

														_dynamics.getDynamicContent(rate_es.getDescription(), rate_en.getDescription())

														)+

												_dynamics.DynamicElement("shortDescriptionRate", "text_area", "text", 

														_dynamics.getDynamicContent(rate_es.getShortDescription(), rate_en.getShortDescription())

														)

												)+

										_dynamics.DynamicElement("benefitsRate", "text_area", "text", 

												_dynamics.getDynamicContent(rate_es.getBenefits(), rate_en.getBenefits())

												)+

										_dynamics.DynamicElement("Restrictions1", "text_area", "text", 

												_dynamics.getDynamicContent(rate_es.getRestrictions(), rate_en.getRestrictions())

												)+

										_dynamics.DynamicElement("occupationRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("rateOnlyIncludesRoomForSingleAndDoubleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForQuadrupleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForSingleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForDoubleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForTripleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForSingleDoubleAndTripleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForSingleDoubleTripleAndQuadrupleOccupancy01", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForDoubleAndTripleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForSingleDoubleAndTripleOccupancy1", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForSingleDoubleAndQuadrupleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("rateOnlyIncludesRoomForDoubleTripleAndQuadrupleOccupancy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("promotionValidUntil", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("promotionIsValid", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("Other1", "text", "keyword", 

														_dynamics.getDynamicContent("", "")

														)

												)+//Fin de ocupacion

										_dynamics.DynamicElement("Benefits1", "selection_break", "keyword", 

												_dynamics.DynamicElement("wirelessInternetRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("breakfastBuffetRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("americanBreakfastBuffetRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("domesticPhoneCallsRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("tipsForBellboysRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("tipsForHousekeepingRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("tipsToWaitersRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("freeParkingRate", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("parkingFee75MXNPerNight", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("specialRateForBreakfastBuffet179MXNTaxesIncluded", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("15OffInFoodAndBeverages", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("15DiscountOnFoodAndNonAlcoholicBeverages", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("15OffInLaundryService", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("freeParking6", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("useToTheWashingAndIroningCenter", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("20OffInRoomService", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("10OffInMealsAndDinners", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("FreeParking10", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("15OffInSpaTreatments", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("welcomeDrinkOnArrival", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("freeAccessToTheGym", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("accessToTheGymAndWirelessInternetForFree", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("10DiscountOnYourStay", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("20DiscountOnYourStay", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("30DiscountOnYourStay", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("40DiscountOnYourStay", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("50DiscountOnYourStay", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("EarnFiestaRewardsPoints", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("AccumulateMotivaAndAppreciatePoints", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("EnjoyFreeBreakfast", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("ReceiveFreeNight", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TheyApply3MonthsWithoutInterestInMinimumPurchasesOf2000PesosOnlyWithAmericanExpressCreditCards", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TheyApply3MonthsWithoutInterestInMinimumPurchasesOf2000PesosOnlyWithAmericanExpressCreditCardsiidt", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("RememberThatYouCanEnjoyOurAllinclusivePlanAtLiveAquaBoutiqueResortPlayaDelCarmenLiveAquaBeachResortCancunGrandFiestaAmericanaLosCabosGrandFiestaAmericanaPuertoVallartaFiestaAmericanaCondesaCancunFiestaAmericanaCozumelFiestaAmericanaPuertoVallartaTheExploreanKohunlichAndTheExploreanCozumelft90", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("Otro9a1y", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)

												)+ // Fin de Beneficios

										_dynamics.DynamicElement("Restrictions", "selection_break", "keyword", 

												_dynamics.DynamicElement("10DeDescuentoEnTuEstancia9ymr", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("20DeDescuentoEnTuEstanciae3qr", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("30DeDescuentoEnTuEstancia4zge", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("40DeDescuentoEnTuEstanciahyvg", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("50DeDescuentoEnTuEstanciaeonn", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("AcumulaPuntosFiestaRewards8tsy", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("AcumulaPuntosMotivaYApreciarea7ab", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("DisfrutaDesayunoGratis8nrt", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("RecibeNocheGratis14rb", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("MinimumNightsStayIsRequired", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TarifaNoAplicaEnOcupacinCudruple2yea", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TarifaNoAplicaEnOcupacinSencillaTripleNiCudruple3f2o", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TarifaNoAplicaEnOcupacinDobleTripleNiCudruple1a29", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TarifaNoAplicaEnOcupacinSencillaNiCudruple3ylu", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TarifaNoAplicaEnOcupacinSencillaNiDoble8eha", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("TarifaNoAplicaEnOcupacinSencillab1uw", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("ToAvoidChargesReviewTheCancellationAndModificationPoliciesBeforeConfirmingyourReservation", "boolean", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("Other2", "text", "keyword", 

														_dynamics.getDynamicContent("", "")

														)

												)+// Fin de Restricciones

										_dynamics.DynamicElement("websiteRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("Descriptions1", "text", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("relatedContractsRate", "text", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("MediaLink1", "document_library", "keyword", 

														_dynamics.DynamicElement("TypeRate2", "list", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

														_dynamics.DynamicElement("Footer", "text", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

														_dynamics.getDynamicContent("", "")

														)

												) +// Fin de web site rate

										_dynamics.DynamicElement("bannerTravelClickRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("headerRate", "text_area", "text", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("Descriptions2", "text_area", "text", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("mediaLinkTravelClickRate", "document_library", "keyword", 

														_dynamics.DynamicElement("TypeRate1", "list", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

														_dynamics.DynamicElement("Piemb2o", "text", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

														_dynamics.DynamicElement("mountRate", "text", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

														_dynamics.getDynamicContent("", "")

														)

												)+ //Fin de Media Link Travel Click

										_dynamics.DynamicElement("promoCodeRate", "text", "keyword", 

												_dynamics.getDynamicContent("", "")

												)+

										_dynamics.DynamicElement("currencyRate", "list", "keyword", 

												_dynamics.getDynamicContent(rate_es.getCurrency(),rate_en.getCurrency())

												)+

										_dynamics.DynamicElement("mediaLinksRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("mediaLinkRate", "document_library", "keyword", 

														_dynamics.DynamicElement("TypeRate3", "list", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

														_dynamics.DynamicElement("PieRate4", "text", "keyword", 

																_dynamics.getDynamicContent("", "")

																)+

																_dynamics.getDynamicContent("", "")

															)

												

												)+

										_dynamics.DynamicElement("bookingDateRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("initialDateBooking", "ddm-date", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("finalDateBooking", "ddm-date", "keyword", 

														_dynamics.getDynamicContent(rate_es.getEnd(), rate_en.getEnd())

														)

												)+

										_dynamics.DynamicElement("travelDateRate", "selection_break", "keyword", 

												_dynamics.DynamicElement("initialDateTravel", "ddm-date", "keyword", 

														_dynamics.getDynamicContent("", "")

														)+

												_dynamics.DynamicElement("finalDateTravel", "ddm-date", "keyword", 

														_dynamics.getDynamicContent("", "")

														)

												)	

									)

							);
					System.out.println(rate);
				return rate;

			}
	
	public static String getXmlBrand(Contents contents_es, Contents contents_en){
		Map<String, String> mapBrand = new HashMap<>();
		
		if(contents_es!=null && !contents_es.equals("")){
			for(Content content : contents_es.getContents()){
				if(content!=null && !content.equals("")){
					for(Brand brand : content.getBrands()){
						mapBrand.put("code_es", brand.getCode());
						mapBrand.put("name_es", brand.getName());
					}
				}
			}
		}
		if(contents_en!=null && !contents_en.equals("")){
			for(Content content : contents_en.getContents()){
				if(content!=null && !content.equals("")){
					for(Brand brand : content.getBrands()){
						mapBrand.put("code_en", brand.getCode());
						mapBrand.put("name_en", brand.getName());
					}
				}
			}
		}
		
		
		
		MappingString _dynamics = new MappingString();
		String brand = _dynamics.DynamicHeader(
				_dynamics.DynamicElement("brand", "selection_break", "keyword", 
						_dynamics.DynamicElement("codeBrand", "text", "keyword", 
								_dynamics.getDynamicContent(mapBrand.get("code_es"), mapBrand.get("code_en"))
								)+
						_dynamics.DynamicElement("nameBrand", "text", "keyword", 
								_dynamics.getDynamicContent(mapBrand.get("name_es"), mapBrand.get("name_en"))
								)+
						_dynamics.DynamicElement("keywordBrand", "text", "keyword", 
								_dynamics.getDynamicContent("", "")
								)+
						_dynamics.DynamicElement("descriptionsBrand", "selection_break", "keyword", 
								_dynamics.DynamicElement("descriptionBrand", "text_area", "text", 
										_dynamics.getDynamicContent("", "")
										)+
								_dynamics.DynamicElement("shortDescriptionBrand", "text_area", "text", 
										_dynamics.getDynamicContent("", "")
										)
								)+
						_dynamics.DynamicElement("sloganBrand", "text", "keyword", 
								_dynamics.getDynamicContent("", "")
								)+
						_dynamics.DynamicElement("productsBrand", "text_area", "text", 
								_dynamics.getDynamicContent("", "")
								)+
						_dynamics.DynamicElement("servicesBrand", "text_area", "text", 
								_dynamics.getDynamicContent("", "")
								)+
						_dynamics.DynamicElement("featureBrand", "text_area", "text", 
								_dynamics.getDynamicContent("", "")
								)+
						_dynamics.DynamicElement("medialinksBrand", "selection_break", "keyword", 
								_dynamics.DynamicElement("MediaLinkFooterBrand", "document_library", "keyword", 
										_dynamics.DynamicElement("typeBrand", "list", "keyword", 
												_dynamics.getDynamicContent("", "")
												)+
										_dynamics.DynamicElement("Pie", "text", "keyword", 
												_dynamics.getDynamicContent("", "")
												)+
										_dynamics.getDynamicContent("", "")
										)+
								_dynamics.DynamicElementRateLink("MediaLinkFooterBrand", "document_library", "keyword", "", "")
								)+
						_dynamics.DynamicElement("ratelinksBrand", "selection_break", "keyword", 
								_dynamics.DynamicElement("ratelinkBrand", "ddm-journal-article", "keyword", 
										_dynamics.getDynamicContent("", "")
										)
								)
						)
				);
		
		return brand;
	}
	
	public void Service(){
		
	}
}