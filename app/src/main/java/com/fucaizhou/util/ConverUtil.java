package com.fucaizhou.util;

public class ConverUtil {

	static class UTM2LatLon {
		static double easting;

		static double northing;

		static int zone;

		static String southernHemisphere = "ACDEFGHJKLM";

		protected static String getHemisphere(String latZone) {
			String hemisphere = "N";
			if (southernHemisphere.indexOf(latZone) > -1) {
				hemisphere = "S";
			}
			return hemisphere;
		}

		public static double[] convertUTMToLatLong(String UTM) {
			double[] latlon = { 0.0, 0.0 };
			String[] utm = UTM.split(" ");
			zone = Integer.parseInt(utm[0]);
			String latZone = utm[1];
			easting = Double.parseDouble(utm[2]);
			northing = Double.parseDouble(utm[3]);
			String hemisphere = getHemisphere(latZone);
			double latitude = 0.0;
			double longitude = 0.0;

			if (hemisphere.equals("S")) {
				northing = 10000000 - northing;
			}
			setVariables();
			latitude = 180 * (phi1 - fact1 * (fact2 + fact3 + fact4)) / Math.PI;

			if (zone > 0) {
				zoneCM = 6 * zone - 183.0;
			} else {
				zoneCM = 3.0;

			}

			longitude = zoneCM - _a3;
			if (hemisphere.equals("S")) {
				latitude = -latitude;
			}

			latlon[0] = latitude;
			latlon[1] = longitude;
			return latlon;

		}

		protected static void setVariables() {
			arc = northing / k0;
			mu = arc / (a * (1 - POW(e, 2) / 4.0 - 3 * POW(e, 4) / 64.0 - 5 * POW(e, 6) / 256.0));

			ei = (1 - POW((1 - e * e), (1 / 2.0))) / (1 + POW((1 - e * e), (1 / 2.0)));

			ca = 3 * ei / 2 - 27 * POW(ei, 3) / 32.0;

			cb = 21 * POW(ei, 2) / 16 - 55 * POW(ei, 4) / 32;
			cc = 151 * POW(ei, 3) / 96;
			cd = 1097 * POW(ei, 4) / 512;
			phi1 = mu + ca * SIN(2 * mu) + cb * SIN(4 * mu) + cc * SIN(6 * mu) + cd * SIN(8 * mu);

			n0 = a / POW((1 - POW((e * SIN(phi1)), 2)), (1 / 2.0));

			r0 = a * (1 - e * e) / POW((1 - POW((e * SIN(phi1)), 2)), (3 / 2.0));
			fact1 = n0 * TAN(phi1) / r0;

			_a1 = 500000 - easting;
			dd0 = _a1 / (n0 * k0);
			fact2 = dd0 * dd0 / 2;

			t0 = POW(TAN(phi1), 2);
			Q0 = e1sq * POW(COS(phi1), 2);
			fact3 = (5 + 3 * t0 + 10 * Q0 - 4 * Q0 * Q0 - 9 * e1sq) * POW(dd0, 4) / 24;

			fact4 = (61 + 90 * t0 + 298 * Q0 + 45 * t0 * t0 - 252 * e1sq - 3 * Q0 * Q0) * POW(dd0, 6) / 720;

			//
			lof1 = _a1 / (n0 * k0);
			lof2 = (1 + 2 * t0 + Q0) * POW(dd0, 3) / 6.0;
			lof3 = (5 - 2 * Q0 + 28 * t0 - 3 * POW(Q0, 2) + 8 * e1sq + 24 * POW(t0, 2)) * POW(dd0, 5) / 120;
			_a2 = (lof1 - lof2 + lof3) / COS(phi1);
			_a3 = _a2 * 180 / Math.PI;

		}

		static double arc;

		static double mu;

		static double ei;

		static double ca;

		static double cb;

		static double cc;

		static double cd;

		static double n0;

		static double r0;

		static double _a1;

		static double dd0;

		static double t0;

		static double Q0;

		static double lof1;

		static double lof2;

		static double lof3;

		static double _a2;

		static double phi1;

		static double fact1;

		static double fact2;

		static double fact3;

		static double fact4;

		static double zoneCM;

		static double _a3;

		static double b = 6356752.314;

		static double a = 6378137;

		static double e = 0.081819191;

		static double e1sq = 0.006739497;

		static double k0 = 0.9996;
	}

	static class LatLon2UTM {

		// Lat Lon to UTM variables

		// equatorial radius
		static double equatorialRadius = 6378137;

		// polar radius
		static double polarRadius = 6356752.314;

		// flattening
		static double flattening = 0.00335281066474748;// (equatorialRadius-polarRadius)/equatorialRadius;

		// inverse flattening 1/flattening
		static double inverseFlattening = 298.257223563;// 1/flattening;

		// Mean radius
		static double rm = POW(equatorialRadius * polarRadius, 1 / 2.0);

		// scale factor
		static double k0 = 0.9996;

		// eccentricity
		static double e = Math.sqrt(1 - POW(polarRadius / equatorialRadius, 2));

		static double e1sq = e * e / (1 - e * e);

		static double n = (equatorialRadius - polarRadius) / (equatorialRadius + polarRadius);

		// r curv 1
		static double rho = 6368573.744;

		// r curv 2
		static double nu = 6389236.914;

		// Calculate Meridional Arc Length
		// Meridional Arc
		static double S = 5103266.421;

		static double A0 = 6367449.146;

		static double B0 = 16038.42955;

		static double C0 = 16.83261333;

		static double D0 = 0.021984404;

		static double E0 = 0.000312705;

		// Calculation Constants
		// Delta Long
		static double p = -0.483084;

		static double sin1 = 4.84814E-06;

		// Coefficients for UTM Coordinates
		static double K1 = 5101225.115;

		static double K2 = 3750.291596;

		static double K3 = 1.397608151;

		static double K4 = 214839.3105;

		static double K5 = -2.995382942;

		static double A6 = -1.00541E-07;

		public static String convertLatLonToUTM(double latitude, double longitude) {
			validate(latitude, longitude);
			String UTM = "";

			setVariables(latitude, longitude);

			String longZone = getLongZone(longitude);
			String latZone = getLatZone(latitude);

			double _easting = getEasting();
			double _northing = getNorthing(latitude);

			UTM = longZone + " " + latZone + " " + ((int) _easting) + " " + ((int) _northing);
			// UTM = longZone + " " + latZone + " " +
			// decimalFormat.format(_easting) +
			// " "+ decimalFormat.format(_northing);

			return UTM;

		}

		private static void validate(double latitude, double longitude) {
			if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude >= 180.0) {
				throw new IllegalArgumentException("Legal ranges: latitude [-90,90], longitude [-180,180).");
			}

		}

		protected static void setVariables(double latitude, double longitude) {
			latitude = degreeToRadian(latitude);
			rho = equatorialRadius * (1 - e * e) / POW(1 - POW(e * SIN(latitude), 2), 3 / 2.0);

			nu = equatorialRadius / POW(1 - POW(e * SIN(latitude), 2), (1 / 2.0));

			double var1;
			if (longitude < 0.0) {
				var1 = ((int) ((180 + longitude) / 6.0)) + 1;
			} else {
				var1 = ((int) (longitude / 6)) + 31;
			}
			double var2 = (6 * var1) - 183;
			double var3 = longitude - var2;
			p = var3 * 3600 / 10000;

			S = A0 * latitude - B0 * SIN(2 * latitude) + C0 * SIN(4 * latitude) - D0 * SIN(6 * latitude) + E0
					* SIN(8 * latitude);

			K1 = S * k0;
			K2 = nu * SIN(latitude) * COS(latitude) * POW(sin1, 2) * k0 * (100000000) / 2;
			K3 = ((POW(sin1, 4) * nu * SIN(latitude) * Math.pow(COS(latitude), 3)) / 24)
					* (5 - POW(TAN(latitude), 2) + 9 * e1sq * POW(COS(latitude), 2) + 4 * POW(e1sq, 2)
							* POW(COS(latitude), 4)) * k0 * (10000000000000000L);

			K4 = nu * COS(latitude) * sin1 * k0 * 10000;

			K5 = POW(sin1 * COS(latitude), 3) * (nu / 6) * (1 - POW(TAN(latitude), 2) + e1sq * POW(COS(latitude), 2))
					* k0 * 1000000000000L;

			A6 = (POW(p * sin1, 6) * nu * SIN(latitude) * POW(COS(latitude), 5) / 720)
					* (61 - 58 * POW(TAN(latitude), 2) + POW(TAN(latitude), 4) + 270 * e1sq * POW(COS(latitude), 2) - 330
							* e1sq * POW(SIN(latitude), 2)) * k0 * (1E+24);

		}

		protected static String getLongZone(double longitude) {
			double longZone = 0;
			if (longitude < 0.0) {
				longZone = ((180.0 + longitude) / 6) + 1;
			} else {
				longZone = (longitude / 6) + 31;
			}
			String val = String.valueOf((int) longZone);
			if (val.length() == 1) {
				val = "0" + val;
			}
			return val;
		}

		private static char[] letters = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Z' };

		private static int[] degrees = { -90, -84, -72, -64, -56, -48, -40, -32, -24, -16, -8, 0, 8, 16, 24, 32, 40,
				48, 56, 64, 72, 84 };

		private static char[] negLetters = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M' };

		private static int[] negDegrees = { -90, -84, -72, -64, -56, -48, -40, -32, -24, -16, -8 };

		private static char[] posLetters = { 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Z' };

		private static int[] posDegrees = { 0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 84 };

		private static int arrayLength = 22;

		public static String getLatZone(double latitude) {
			int latIndex = -2;
			int lat = (int) latitude;

			if (lat >= 0) {
				int len = posLetters.length;
				for (int i = 0; i < len; i++) {
					if (lat == posDegrees[i]) {
						latIndex = i;
						break;
					}

					if (lat > posDegrees[i]) {
						continue;
					} else {
						latIndex = i - 1;
						break;
					}
				}
			} else {
				int len = negLetters.length;
				for (int i = 0; i < len; i++) {
					if (lat == negDegrees[i]) {
						latIndex = i;
						break;
					}

					if (lat < negDegrees[i]) {
						latIndex = i - 1;
						break;
					} else {
						continue;
					}

				}

			}

			if (latIndex == -1) {
				latIndex = 0;
			}
			if (lat >= 0) {
				if (latIndex == -2) {
					latIndex = posLetters.length - 1;
				}
				return String.valueOf(posLetters[latIndex]);
			} else {
				if (latIndex == -2) {
					latIndex = negLetters.length - 1;
				}
				return String.valueOf(negLetters[latIndex]);

			}
		}

		protected static double getNorthing(double latitude) {
			double northing = K1 + K2 * p * p + K3 * POW(p, 4);
			if (latitude < 0.0) {
				northing = 10000000 + northing;
			}
			return northing;
		}

		protected static double getEasting() {
			return 500000 + (K4 * p + K5 * POW(p, 3));
		}

	}

	public static double degreeToRadian(double degree) {
		return degree * Math.PI / 180;
	}

	public static double radianToDegree(double radian) {
		return radian * 180 / Math.PI;
	}

	private static double POW(double a, double b) {
		return Math.pow(a, b);
	}

	private static double SIN(double value) {
		return Math.sin(value);
	}

	private static double COS(double value) {
		return Math.cos(value);
	}

	private static double TAN(double value) {
		return Math.tan(value);
	}

	public static String latLon2UTM(double latitude, double longitude) {
		return LatLon2UTM.convertLatLonToUTM(latitude, longitude);
	}

	public static double[] utm2LatLon(String UTM) {

		return UTM2LatLon.convertUTMToLatLong(UTM);
	}

}
