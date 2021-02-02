/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.hotel;


import dao.hotel.HotelAccount;
import dao.hotel.HotelServiceDAO;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
/**
 *
 * @author A00288569
 */
@Path("/HotelRWS")
public class HotelResource {
    
    @Context
    private UriInfo context;
    
    // connect to the db
    private HotelServiceDAO dao = new HotelServiceDAO();
    
    public HotelResource(){
        
    }
     @POST   // POST is always on a collection; meaning is "add"
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response postXml(HotelAccount HotelAccount){
        // get the next account number for the branch that is
        // in the request message
        int nextAccountNumber = 
                dao.getNextCustomerNumber(HotelAccount.getBranchCode());
        String nextAccountNumberString = 
                Integer.toString(nextAccountNumber);
        HotelAccount.setAccountNo(nextAccountNumberString);
        
          // db insert with the updated 'bankAccount'
        dao.addHotelAccount(HotelAccount);
        
        return Response
                .status(Response.Status.CREATED) 
                .header("Location", 
                        String.format("%s/%s/%s", 
                                context.getAbsolutePath().toString(),
                                HotelAccount.getBranchCode(),
                                HotelAccount.getAccountNo()))
                .entity(HotelAccount)
                .build();
    
        //    .header("Location", 
//          "http://localhost:8080/SOA5_BankServices_2018/rest/BankRWS/123456/12345679")
}
@GET    // GET on a collection URI means "get them all"
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllAccounts(){
        List<HotelAccount> HotelAccounts =
                dao.getAllAccounts();
        
        GenericEntity<List<HotelAccount>> entity;
        entity = new GenericEntity<List<HotelAccount>>(HotelAccounts){};
            
        return Response
                .status(Response.Status.OK)
                .entity(entity)
                .build();
    }
    @HEAD   // HEAD on any URI means "GET without entity body"
    public Response doHeadCollection(){
        return Response
                .noContent()
                .status(Response.Status.NO_CONTENT)
                .build();
    }
    
@OPTIONS    // OPTIONS on any URI means "what verbs are supported?"
    public Response doOptionsCollection(){
        // what are the HTTP verbs (API) allowed on this URI?
        Set<String> api = new TreeSet<>();
        api.add("GET");
        api.add("POST");
        api.add("DELETE");
        api.add("HEAD");
        
        return Response
                .noContent()
                .allow(api)
                .build();
        
    }
    
 @DELETE // DELETE on a collection URI means "delete them all"
    public Response deleteAllHotelAccounts(){
        dao.deleteAllAccounts();
        
        return Response
                .noContent()
                .build();
    }
    
    @DELETE
       @Path("{branchCode}/{accountNo}")// DELETE one"
       public Response deleteOneAccount(@PathParam("branchCode") String branchCode, @PathParam("accountNo") String accountNo){
           dao.deleteHotelAccount(branchCode, accountNo);
           return Response
                   .noContent()
                   .build();
       }
     // here we turn our attention to the individual resource
    @GET
    @Path("{branchCode}/{accountNumber}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAccountDetails(  @PathParam("branchCode") String branchCode,
                                        @PathParam("accountNumber") String accountNumber){
        HotelAccount hotelAccount =
                dao.getAccountDetails(branchCode, accountNumber);
        if(hotelAccount == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("<accountNotFound />")
                    .build();
        }else{
            return Response
                    .status(Response.Status.OK)
                    .entity(hotelAccount)
                    .build();
        }
        
    }
    @HEAD   // HEAD on any URI means "GET without entity body"
    @Path("{branchCode}/{accountNumber}")
    public Response getMetadata(){
        return Response
                .noContent()
                .status(Response.Status.NO_CONTENT)
                .build();
    }
    @OPTIONS    // OPTIONS on any URI means "what verbs are supported?"
    @Path("{branchCode}/{accountNumber}")
    public Response getAPI(){
        // what are the HTTP verbs (API) allowed on this URI?
        Set<String> api = new TreeSet<>();
        api.add("GET");
        api.add("PUT");
        api.add("DELETE");
        api.add("HEAD");
        
        return Response
                .noContent()
                .allow(api)
                .build();
        
    }
    @PUT
    @Path("{branchCode}/{accountNumber}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response putHotelAccount(HotelAccount updatedHotelAccount,
                                   @PathParam("branchCode") String branchCode,
                                   @PathParam("accountNumber") String accountNumber){
        
        // The 'updatedBankAccount' will have its own 'branchCode'
        // and 'accountNumber'. Make sure they match the 'branchCode'
        // and 'accountNumber' in the URI.
        if(branchCode.equals(updatedHotelAccount.getBranchCode())
                && accountNumber.equals(updatedHotelAccount.getAccountNo())){
            // OK
            // HTTP PUT:
            //    if the bank account is not in db, insert it
            //    if bank account is in db, update it
            
if(dao.getAccountDetails(branchCode, accountNumber) == null){
                // bank account is not in db, insert it
                // Here, we do NOT get the next account number (like in POST);
                // this is because the client (somehow) knows the URI to use.
                dao.addHotelAccount(updatedHotelAccount);
                return Response
                        .status(Response.Status.CREATED)
                        .header("Location", context.getAbsolutePath().toString())
                        .entity(updatedHotelAccount)
                        .build();
    }else{
                // bank account is in db, update it
                dao.updateHotelAccount(updatedHotelAccount);
                return Response
                        .status(Response.Status.OK)
                        .entity(updatedHotelAccount)
                        .build();
            }
            
        }else{
            // error - xml in the entity body conflicts with
            //         the URI parameter values
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("<mismatchError />")
                    .build();
        }
    }
}