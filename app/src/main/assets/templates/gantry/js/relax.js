jQuery.noty.themes.relax = {
    name    : 'relax',
    helpers : {},
    modal   : {
        css: {
            position       : 'fixed',
            width          : '100%',
            height         : '100%',
            backgroundColor: '#000',
            zIndex         : 10000,
            opacity        : 0.6,
            display        : 'none',
            left           : 0,
            top            : 0
        }
    },
    style   : function() {

        this.jQuerybar.css({
            overflow    : 'hidden',
            margin      : '4px 0',
            borderRadius: '2px'
        });

        this.jQuerymessage.css({
            fontSize  : '14px',
            lineHeight: '16px',
            textAlign : 'center',
            padding   : '10px',
            width     : 'auto',
            position  : 'relative'
        });

        this.jQuerycloseButton.css({
            position  : 'absolute',
            top       : 4, right: 4,
            width     : 10, height: 10,
            background: "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAQAAAAnOwc2AAAAxUlEQVR4AR3MPUoDURSA0e++uSkkOxC3IAOWNtaCIDaChfgXBMEZbQRByxCwk+BasgQRZLSYoLgDQbARxry8nyumPcVRKDfd0Aa8AsgDv1zp6pYd5jWOwhvebRTbzNNEw5BSsIpsj/kurQBnmk7sIFcCF5yyZPDRG6trQhujXYosaFoc+2f1MJ89uc76IND6F9BvlXUdpb6xwD2+4q3me3bysiHvtLYrUJto7PD/ve7LNHxSg/woN2kSz4txasBdhyiz3ugPGetTjm3XRokAAAAASUVORK5CYII=)",
            display   : 'none',
            cursor    : 'pointer'
        });

        this.jQuerybuttons.css({
            padding        : 5,
            textAlign      : 'right',
            borderTop      : '1px solid #ccc',
            backgroundColor: '#fff'
        });

        this.jQuerybuttons.find('button').css({
            marginLeft: 5
        });

        this.jQuerybuttons.find('button:first').css({
            marginLeft: 0
        });

        this.jQuerybar.on({
            mouseenter: function() {
                jQuery(this).find('.noty_close').stop().fadeTo('normal', 1);
            },
            mouseleave: function() {
                jQuery(this).find('.noty_close').stop().fadeTo('normal', 0);
            }
        });

        switch(this.options.layout.name) {
            case 'top':
                this.jQuerybar.css({
                    borderBottom: '2px solid #eee',
                    borderLeft  : '2px solid #eee',
                    borderRight : '2px solid #eee',
                    borderTop   : '2px solid #eee',
                    boxShadow   : "0 2px 4px rgba(0, 0, 0, 0.1)"
                });
                break;
            case 'topCenter':
            case 'center':
            case 'bottomCenter':
            case 'inline':
                this.jQuerybar.css({
                    border   : '1px solid #eee',
                    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)"
                });
                this.jQuerymessage.css({fontSize: '13px', textAlign: 'center'});
                break;
            case 'topLeft':
            case 'topRight':
            case 'bottomLeft':
            case 'bottomRight':
            case 'centerLeft':
            case 'centerRight':
                this.jQuerybar.css({
                    border   : '1px solid #eee',
                    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)"
                });
                this.jQuerymessage.css({fontSize: '13px', textAlign: 'left'});
                break;
            case 'bottom':
                this.jQuerybar.css({
                    borderTop   : '2px solid #eee',
                    borderLeft  : '2px solid #eee',
                    borderRight : '2px solid #eee',
                    borderBottom: '2px solid #eee',
                    boxShadow   : "0 -2px 4px rgba(0, 0, 0, 0.1)"
                });
                break;
            default:
                this.jQuerybar.css({
                    border   : '2px solid #eee',
                    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)"
                });
                break;
        }

        switch(this.options.type) {
            case 'alert':
            case 'notification':
                this.jQuerybar.css({backgroundColor: '#FFF', borderColor: '#dedede', color: '#444'});
                break;
            case 'warning':
                this.jQuerybar.css({backgroundColor: '#FFEAA8', borderColor: '#FFC237', color: '#826200'});
                this.jQuerybuttons.css({borderTop: '1px solid #FFC237'});
                break;
            case 'error':
                this.jQuerybar.css({backgroundColor: '#FF8181', borderColor: '#e25353', color: '#FFF'});
                this.jQuerymessage.css({fontWeight: 'bold'});
                this.jQuerybuttons.css({borderTop: '1px solid darkred'});
                break;
            case 'information':
                this.jQuerybar.css({backgroundColor: '#78C5E7', borderColor: '#3badd6', color: '#FFF'});
                this.jQuerybuttons.css({borderTop: '1px solid #0B90C4'});
                break;
            case 'success':
                this.jQuerybar.css({backgroundColor: '#BCF5BC', borderColor: '#7cdd77', color: 'darkgreen'});
                this.jQuerybuttons.css({borderTop: '1px solid #50C24E'});
                break;
            default:
                this.jQuerybar.css({backgroundColor: '#FFF', borderColor: '#CCC', color: '#444'});
                break;
        }
    },
    callback: {
        onShow : function() {

        },
        onClose: function() {

        }
    }
};
