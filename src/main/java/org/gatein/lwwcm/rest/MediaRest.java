package org.gatein.lwwcm.rest;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.File;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
    REST point to serve uploads files
 */
@Path("/u")
public class MediaRest {
    private static final Logger log = Logger.getLogger(MediaRest.class.getName());

    @Inject
    private WcmService wcm;

    @GET
    @Path("/{id:\\d*}")
    @Produces("*/*")
    public Response getUpload(@PathParam("id") Long id) {
        UserWcm userWcm = new UserWcm("anonymous");
        File output = null;
        try {
            Upload upload = wcm.findUpload(new Long(id), userWcm);
            if (upload != null) {
                String dirPath = null;
                String fullPath = null;
                if (System.getProperty(Wcm.UPLOADS.FOLDER) == null) {
                    dirPath = System.getProperty(Wcm.UPLOADS.DEFAULT) + "/lwwcm/uploads";
                } else {
                    dirPath = System.getProperty(Wcm.UPLOADS.FOLDER);
                }
                File dir = new File(dirPath);
                if (!dir.exists() && !dir.mkdir()) {
                    throw new WcmException("Cannot read dir: " + Wcm.UPLOADS.FOLDER);
                }
                fullPath = dirPath + File.separator + upload.getStoredName();
                output = new File(fullPath);
                return Response.ok(output, upload.getMimeType()).build();
            } else {
                log.warning("Upload ID: " + id + " not found.");
            }
        } catch (WcmException e) {
            log.warning("Error accessing upload.");
            e.printStackTrace();
        } catch (Exception e) {
            log.warning("Error downloading file.");
            e.printStackTrace();
        }
        return Response.noContent().build();
    }

}
