package org.workcraft.plugins.balsa.handshakebuilder;

import org.workcraft.plugins.balsa.handshakestgbuilder.HandshakeStgBuilder;
import org.workcraft.plugins.balsa.handshakestgbuilder.StgHandshake;

public interface Handshake
{
	public StgHandshake buildStg(HandshakeStgBuilder builder);
}
