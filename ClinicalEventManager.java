package eu.dedalus.sop.o4c.servlets.atmosphere.clinicalevent;

import java.io.IOException;

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.atmosphere.interceptor.HeartbeatInterceptor;
import org.atmosphere.kafka.KafkaBroadcaster;

import com.owlike.genson.Genson;

import eu.dedalus.sop.log4j.LogManagerSOP;
import eu.dedalus.sop.log4j.LoggerSOP;
import eu.dedalus.sop.o4c.models.common.atmosphere.DataEntryOperation;

@ManagedService(path = "/clinicalevent/manager")
@AtmosphereHandlerService(path = "/clinicalevent/manager", 
broadcaster = KafkaBroadcaster.class,
interceptors = {AtmosphereResourceLifecycleInterceptor.class, 
        BroadcastOnPostAtmosphereInterceptor.class ,HeartbeatInterceptor.class  })
public final class ClinicalEventManager {

	private static final LoggerSOP logger = LogManagerSOP.getLogger(ClinicalEventManager.class);
	private static final Genson genson = new Genson();

	@Ready
	public final void onReady(final AtmosphereResource r) {
		logger.info("Browser " + r.uuid() + " connected.");
	}
	@Disconnect
	public final void onDisconnect(final AtmosphereResourceEvent event) {
		if (event.isCancelled())
			logger.info("Browser " + event.getResource().uuid() + " unexpectedly disconnected");
		else if (event.isClosedByClient())
			logger.info("Browser " + event.getResource().uuid() + " closed the connection");
	}

	@Message(encoders = {
			ClinicalEventManagerEncoderDecoder.class
	}, decoders = {
			ClinicalEventManagerEncoderDecoder.class
	})
	public final DataEntryOperation onMessage(final DataEntryOperation message) throws IOException {
		logger.info(message.getUser() + " just sent " + genson.serialize(message));
		return message;
	}

}