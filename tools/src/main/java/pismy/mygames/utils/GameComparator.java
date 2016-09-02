package pismy.mygames.utils;

import java.util.ArrayList;
import java.util.List;

import pismy.mygames.dat.IGame;
import pismy.mygames.dat.IRom;
import pismy.mygames.utils.GameComparator.Comparison.Difference;

public class GameComparator {
	public enum Status {
		matches, compatible, can_be_fixed, incompatible
	}

	public interface Comparison {
		Status getStatus();

		boolean fix();

		List<Difference> getDifferences();

		String dump(Status minLevel, boolean packed);
		
		public interface Difference {
			Status getStatus();

			boolean fix();
			
		}
	}

	private static Difference compare(IRom rom, IRom expected) {
		if (rom.getSize() != expected.getSize())
			return new WrongSize(rom, expected);
		// try checksums
		if (rom.getCrc() != null && expected.getCrc() != null
				&& !rom.getCrc().equals(expected.getCrc()))
			return new WrongCrc(rom, expected);
		if (rom.getMd5() != null && expected.getMd5() != null
				&& !rom.getMd5().equals(expected.getMd5()))
			return new WrongMd5(rom, expected);
		if (rom.getSha1() != null && expected.getSha1() != null
				&& !rom.getSha1().equals(expected.getSha1()))
			return new WrongSha1(rom, expected);
		return null;
		// if (!getName().equals(other.getName()))
		// return false;
	}

	public static Comparison compare(IGame<?> game, IGame<?> ref) {
		List<? extends IRom> roms = new ArrayList<IRom>(game.getRoms());
		List<? extends IRom> refRoms = new ArrayList<IRom>(ref.getRoms());
		ComparisonImpl ret = new ComparisonImpl();
		// 1: find exact names
		for (int i = roms.size() - 1; i >= 0; i--) {
			IRom rom = roms.get(i);
			for (IRom refRom : refRoms) {
				if (refRom.getName().equals(rom.getName())) {
					Difference diff = compare(rom, refRom);
					if (diff != null) {
						ret.differences.add(diff);
					}
					// rom treated (remove from both lists)
					refRoms.remove(refRom);
					roms.remove(i);
					break;
				}
			}
		}
		// 2: find wrong names
		for (int i = roms.size() - 1; i >= 0; i--) {
			IRom rom = roms.get(i);
			for (IRom refRom : refRoms) {
				if (compare(rom, refRom) == null) {
					// rom treated (remove from both lists)
					roms.remove(i);
					refRoms.remove(refRom);
					ret.differences.add(new WrongName(rom, refRom));
					break;
				}
			}
		}

		// 3: unnecessary/missing roms
		for (IRom rom : roms) {
			ret.differences.add(new UnnecessaryRom(rom));
		}
		for (IRom rom : refRoms) {
			ret.differences.add(new MissingRom(rom, false /* TODO */));
		}

		return ret;
	}

	private static class ComparisonImpl implements Comparison {
		List<Difference> differences = new ArrayList<GameComparator.Comparison.Difference>();

		@Override
		public List<Difference> getDifferences() {
			return differences;
		}

		@Override
		public Status getStatus() {
			int status = Status.matches.ordinal();
			for (Difference diff : differences) {
				status = Math.max(status, diff.getStatus().ordinal());
			}
			return Status.values()[status];
		}

		@Override
		public boolean fix() {
			for (Difference diff : differences) {
				if (!diff.fix())
					return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return dump(Status.compatible, false);
		}
		public String dump(Status minLevel, boolean packed) {
			if (differences.size() == 0) {
				return "matches";
			}
			StringBuffer sb = new StringBuffer(getStatus().toString());
			int i=0;
			for (Difference diff : differences) {
				if(diff.getStatus().compareTo(minLevel) < 0)
					continue;
				if(packed) {
					if(i == 0)
						sb.append(": ");
					else
						sb.append(", ");
				} else {
					sb.append("\n - ");
				}
				
				sb.append(diff);
				i++;
			}
			return sb.toString();
		}
	}

	private static class MissingRom implements Difference {
		final IRom rom;
		final boolean optional;

		public MissingRom(IRom rom, boolean optional) {
			this.rom = rom;
			this.optional = optional;
		}

		@Override
		public Status getStatus() {
			return optional ? Status.compatible : Status.incompatible;
		}

		@Override
		public boolean fix() {
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] missing"
					+ (optional ? " (optional)" : "");
		}
	}

	private static class UnnecessaryRom implements Difference {
		final IRom rom;

		public UnnecessaryRom(IRom rom) {
			this.rom = rom;
		}

		@Override
		public Status getStatus() {
			return Status.compatible;
		}

		@Override
		public boolean fix() {
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] unnecessary";
		}
	}

	private static class WrongName implements Difference {
		final IRom rom;
		final IRom expected;

		public WrongName(IRom rom, IRom expected) {
			this.rom = rom;
			this.expected = expected;
		}

		@Override
		public Status getStatus() {
			return Status.can_be_fixed;
		}

		@Override
		public boolean fix() {
			// TODO
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] wrong name (expected: " + expected.getName() + ")";
		}
	}

	private static class WrongSize implements Difference {
		final IRom rom;
		final IRom expected;

		public WrongSize(IRom rom, IRom expected) {
			this.rom = rom;
			this.expected = expected;
		}

		@Override
		public Status getStatus() {
			return Status.incompatible;
		}

		@Override
		public boolean fix() {
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] wrong size (" + rom.getSize()
					+ "/expected: " + expected.getSize() + ")";
		}
	}

	private static class WrongCrc implements Difference {
		final IRom rom;
		final IRom expected;

		public WrongCrc(IRom rom, IRom expected) {
			this.rom = rom;
			this.expected = expected;
		}

		@Override
		public Status getStatus() {
			return Status.incompatible;
		}

		@Override
		public boolean fix() {
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] wrong CRC (" + rom.getCrc()
					+ "/expected: " + expected.getCrc() + ")";
		}
	}

	private static class WrongSha1 implements Difference {
		final IRom rom;
		final IRom expected;

		public WrongSha1(IRom rom, IRom expected) {
			this.rom = rom;
			this.expected = expected;
		}

		@Override
		public Status getStatus() {
			return Status.incompatible;
		}

		@Override
		public boolean fix() {
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] wrong SHA1 (" + rom.getSha1()
					+ "/expected: " + expected.getSha1() + ")";
		}
	}

	private static class WrongMd5 implements Difference {
		final IRom rom;
		final IRom expected;

		public WrongMd5(IRom rom, IRom expected) {
			this.rom = rom;
			this.expected = expected;
		}

		@Override
		public Status getStatus() {
			return Status.incompatible;
		}

		@Override
		public boolean fix() {
			return false;
		}

		@Override
		public String toString() {
			return "[" + rom.getName() + "] wrong MD5 (" + rom.getMd5()
					+ "/expected: " + expected.getMd5() + ")";
		}
	}

}
