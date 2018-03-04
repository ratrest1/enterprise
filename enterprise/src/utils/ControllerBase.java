/**
 * 		ControllerBase:
 * 			The base class for all controllers.
 */
package utils;

import db.AuthorGateway;

public class ControllerBase 
{
		private ControllerLoader loader;		        //Loader for loading views/controllers
		protected GatewayBase viewLocation;		//location of view xml file on the disk
		
		/**
		 * 		Constructor
		 * @param authorGateway the location of view xml file on the disk
		 */
		protected ControllerBase(GatewayBase authorGateway){
			setViewLocation(authorGateway);
			loader = ControllerLoader.getInstance();
		}

		//Accessors
		public GatewayBase getViewLocation() {
			return viewLocation;
		}

		public void setViewLocation(GatewayBase viewLocation) {
			this.viewLocation = viewLocation;
		}

		public ControllerLoader getLoader() {
			return loader;
		}
}
