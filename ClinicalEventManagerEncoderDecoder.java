package eu.dedalus.sop.o4c.servlets.atmosphere.clinicalevent;

import eu.dedalus.sop.o4c.models.common.atmosphere.DataEntryOperation;

import com.owlike.genson.Genson;
import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class ClinicalEventManagerEncoderDecoder implements Encoder<DataEntryOperation, String>, Decoder<String, DataEntryOperation> {

	private static final Genson genson = new Genson();

	@Override
	public DataEntryOperation decode(final String message) {
		try {
			DataEntryOperation dataEntryOperation = genson.deserialize(message, DataEntryOperation.class);
			dataEntryOperation.setDateTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			return dataEntryOperation;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String encode(final DataEntryOperation message) {
		try {
			return genson.serialize(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}