package cn.gucas.ml.recsys.attack.profile;

import java.util.HashMap;

import cn.gucas.ml.recsys.attack.Constants;
import cn.gucas.ml.recsys.attack.Constants.Rate;
import cn.gucas.ml.recsys.attack.utils.NormGenerator;

public class AttackProfile {
	private int type;
	private int selectSize;
	private int fillerSize;
	private int uid;
	private int tiid;
	private boolean push;
	private HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();

	public AttackProfile(int uid, int tiid, boolean push, int type,
			int selectSize, int fillerSize) {
		this.uid = uid;
		this.type = type;
		this.selectSize = selectSize;
		this.fillerSize = fillerSize;
		this.tiid = tiid;
		this.push = push;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSelectSize() {
		return selectSize;
	}

	public void setSelectSize(int selectSize) {
		this.selectSize = selectSize;
	}

	public int getFillerSize() {
		return fillerSize;
	}

	public void setFillerSize(int fillerSize) {
		this.fillerSize = fillerSize;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getTiid() {
		return tiid;
	}

	public void setTiid(int tiid) {
		this.tiid = tiid;
	}

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

	public HashMap<Integer, Integer> getItems() {
		return items;
	}

	public void setItems(HashMap<Integer, Integer> items) {
		this.items = items;
	}

	public void generate() {
		// 1. target item
		if (push) {
			items.put(tiid, Rate.MAX);
		} else {
			items.put(tiid, Rate.MIN);
		}

		// 2. check type
		if (type == Constants.Type.AVERAGE_ATTACK) {
			// filler items
			int fSize = fillerSize * Constants.ITEM_NUM / 100;

			int i = 0;
			while (i < fSize) {
				int fiid = (int) (1 + Math.random() * Constants.ITEM_NUM);
				if (items.containsKey(fiid)) {
					continue;
				}

				// round
				int rate = (int) Math.round(Constants.ITEM_AVERAGE[fiid - 1]);
				items.put(fiid, rate);

				++i;
			}
		} else if (type == Constants.Type.BANDWAGON_ATTACK) {
			// selected items
			for (int i = 0; i < Constants.BEST_SELLER_SIZE; ++i) {
				int fiid = Constants.BEST_SELLER[i];
				int rate = (int) Math.round(Constants.ITEM_AVERAGE[fiid - 1]);
				items.put(fiid, rate);
			}

			// filler items
			int fSize = fillerSize * Constants.ITEM_NUM / 100;

			// Gaussian distribution with average value: MIU, variance value:
			// SIGMA
			NormGenerator generator = new NormGenerator(Constants.MIU,
					Constants.SIGMA);

			int i = 0;
			while (i < fSize) {
				int fiid = (int) (1 + Math.random() * Constants.ITEM_NUM);
				if (items.containsKey(fiid)) {
					continue;
				}
				int rate = generator.nextGaussian();
				items.put(fiid, rate);

				++i;
			}
		} else if (type == Constants.Type.LOVE_HATE_ATTACK) {

		} else if (type == Constants.Type.RANDOM_ATTACK) {
			// filler items
			int fSize = fillerSize * Constants.ITEM_NUM / 100;

			// Gaussian distribution with average value: MIU, variance value:
			// SIGMA
			NormGenerator generator = new NormGenerator(Constants.MIU,
					Constants.SIGMA);

			int i = 0;
			while (i < fSize) {
				int fiid = (int) (1 + Math.random() * Constants.ITEM_NUM);
				if (items.containsKey(fiid)) {
					continue;
				}
				int rate = generator.nextGaussian();
				items.put(fiid, rate);

				++i;
			}
		} else if (type == Constants.Type.SEGMENT_ATTACK) {

		} else {

		}
	}
}