package com.nairbspace.octoandroid.data.net.stream;

import java.io.DataInputStream;
import java.io.InputStream;

public class MjpegInputStream extends DataInputStream {

    public MjpegInputStream(InputStream in) {
        super(in);
    }
}
