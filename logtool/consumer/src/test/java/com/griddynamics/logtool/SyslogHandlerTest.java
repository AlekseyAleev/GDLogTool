package com.griddynamics.logtool;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.group.ChannelGroup;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

public class SyslogHandlerTest {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
    private final DateTimeFormatter indexFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private Storage mockedStorage;
    private SearchServer mockedSearch;
    private SyslogServerHandler testHandler;
    private ChannelGroup mockedChannelGroup;

    @Before
    public void init() {
        mockedStorage = mock(Storage.class);
        mockedSearch = mock(SearchServer.class);
        mockedChannelGroup = mock(ChannelGroup.class);
        List<Processor> procList = new LinkedList<Processor>();
        procList.add(new Processor("Processor1", ".*bullshit.*", "simple"));
        procList.add(new Processor("Processor1", ".*2011-09-07.*", "containsDate"));
        procList.add(new Processor("Processor1", ".*bullshit!!bullshit!!.*", "lessSimple"));
        EventProcessor spied = new EventProcessor();
        spied.setProcessors(procList);
        EventProcessor mockedEventProcessor = spy(spied);
        String regexp = "[^|]*[ ]*[|][ ]*([a-zA-Z0-9_\\.\\-]+)[ ]*[|][ ]*([a-zA-Z0-9_\\.\\-]+)[ ]*[|][ ]*((?:.+[ ]*[|][ ]*)?((?:(?:[0-3][0-9] [JFMASOND][a-z]{2} 2[0-9]{3})?|(?:2[0-9]{3}-[0-1][0-9]-[0-3][0-9])?)? ?[0-2][0-9]:[0-5][0-9]:[0-5][0-9],[0-9]{3})[ ]*[|][ ]*.+)[\n]?";
        Map<String, Integer> groups = new HashMap<String, Integer>();
        groups.put("application", 1);
        groups.put("instance", 2);
        groups.put("content", 3);
        groups.put("timestamp", 4);
        testHandler = new SyslogServerHandler(mockedStorage, mockedSearch, regexp, groups, mockedEventProcessor, mockedChannelGroup);
    }

    @Test
    public void testMessageRecieved() {

        InetSocketAddress testAddress = new InetSocketAddress("localhost", 4445);

        ChannelHandlerContext testCtx = mock(ChannelHandlerContext.class);
        Channel testChannel = mock(Channel.class);
        when(testChannel.getLocalAddress()).thenReturn(testAddress);
        when(testCtx.getChannel()).thenReturn(testChannel);

        String message = "bullshit!!";
        String someMsg = "";
        for(int i = 0; i < 80; i++) {
            someMsg = someMsg + message;
        }

        someMsg = "2011-09-07 14:31:02,129|" + someMsg;
        String endMessage = "| SomeApp | SomeInst|" + someMsg;
        byte[] sendData = endMessage.getBytes();
        ChannelBuffer cb = ChannelBuffers.copiedBuffer(sendData);
        MessageEvent testMessage = mock(MessageEvent.class);
        when(testMessage.getRemoteAddress()).thenReturn(testAddress);
        System.out.println(testAddress.getAddress());
        when(testMessage.getMessage()).thenReturn(cb);
        String[] pathToVerify = new String[3];
        pathToVerify[0] = "SomeApp";
        pathToVerify[1] = "localhost";
        pathToVerify[2] = "SomeInst";
        testHandler.messageReceived(testCtx, testMessage);
        verify(mockedStorage, times(1)).addMessage(pathToVerify, "2011-09-07T14:31:02" , someMsg );

        Map<String, Object> mapToVerify = new LinkedHashMap<String, Object>();
        mapToVerify.put("application", pathToVerify[0]);
        mapToVerify.put("host", pathToVerify[1]);
        mapToVerify.put("instance", pathToVerify[2]);
        mapToVerify.put("content", someMsg);
        mapToVerify.put("timestamp", "2011-09-07T14:31:02" + "Z");
        List<String> tags = new LinkedList<String>();
        tags.add("simple");
        tags.add("containsDate");
        tags.add("lessSimple");
        mapToVerify.put("tags", tags);
        verify(mockedSearch).index(mapToVerify);

    }
}
