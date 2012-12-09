JowfsClient
===========
JowfsClient is a Java client library for the owserver found in the OWFS project (http://www.owfs.org)

Testing
======
This library contains both unit and integration test classes. If you want to run integration test you have to
configure it.
These tests use owfs server that has to be deployed and running.
All you have to is to add simple properties file testng-integration.properties to project base directory indicating your
real owfs server and device ids.
Your file has to contain variables listed below:
* owfs.hostname=192.168.1.2
* owfs.port=4304
* owfs.device.ds2408.output=29.07960B00000019
* owfs.device.ds2408.input=29.DD940B00000091

License (BSD)
=======
The compilation of software known from the jowfsclient project is distributed 
under the following terms: 

All files are subject to Copyright (c) by their respective authors. 
All rights reserved. 
See individual source files for details. 

* Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met: 

* Redistributions of source code must retain the above copyright notice, this
	list of conditions and the following disclaimer. 

* Redistributions in binary form must reproduce the above copyright notice,
	this list of conditions and the following disclaimer in the documentation
	and/or other materials provided with the distribution. 

The name of the authors may not be used to endorse or promote products derived
from this software without specific prior written permission. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
