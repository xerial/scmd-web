create table fwdrevgoassoc (
        param   text,
        goid    text,
        fwd     int,
        high    int,
        ingo    int,
        inabnorm        int,
        intarget        int,
        pvalue  float8,
        ratio   float8 );
\copy fwdrevgoassoc from ../fwdrevhtml/fwdlow.tab
\copy fwdrevgoassoc from ../fwdrevhtml/revhigh.tab
\copy fwdrevgoassoc from ../fwdrevhtml/revlow.tab
\copy fwdrevgoassoc from ../fwdrevhtml/fwdhigh.tab
