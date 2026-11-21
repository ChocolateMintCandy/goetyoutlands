package com.mint.goetyoutlands.client.screen;

import com.mint.goetyoutlands.Config;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class NecromancerConfigScreen extends Screen {
    private static final String KEY = Config.NECROMANCERS_KEY;
    private static final Component SERVER_NOTE = Component.translatable(KEY + ".serverNote");
    private static final int LABEL_WIDTH = 120, COLUMN_WIDTH = 84, ROW_HEIGHT = 24;

    private final Screen parent;
    private final Column[] columns;
    private Button save;

    public NecromancerConfigScreen(Screen parent) {
        super(Component.translatable(KEY));
        this.parent = parent;
        Column illager = new Column(Config.ILLAGER_NECROMANCER);
        columns = goety_outlands.hasArsNouveau() ? new Column[]{illager, new Column(Config.ARCHMAGE_NECROMANCER)} : new Column[]{illager};
    }

    private int left() { return (width - LABEL_WIDTH - columns.length * COLUMN_WIDTH) / 2; }
    private int top() { return height / 2 - 38; }

    @Override
    protected void init() {
        for (int col = 0; col < columns.length; col++) {
            Column column = columns[col];
            int x = left() + LABEL_WIDTH + col * COLUMN_WIDTH;
            for (int row = 0; row < column.fields.length; row++) {
                Field<?> field = column.fields[row];
                String draft = field.box == null ? field.value.get().toString() : field.box.getValue();
                Component name = Component.translatable(KEY + ".fieldName", column.name, field.label);
                field.box = addRenderableWidget(new EditBox(font, x, top() + row * ROW_HEIGHT, COLUMN_WIDTH - 8, 18, name));
                field.box.setValue(draft);
                field.box.setTooltip(field.tooltip);
                field.box.setResponder(text -> validateFields());
            }
        }
        int y = top() + 94;
        save = addRenderableWidget(Button.builder(Component.translatable(KEY + ".save"), button -> saveAndClose())
                .bounds(width / 2 - 104, y, 100, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
                .bounds(width / 2 + 4, y, 100, 20).build());
        validateFields();
    }

    private void validateFields() {
        boolean valid = true;
        for (Column column : columns) {
            for (Field<?> field : column.fields) {
                if (!field.validate()) valid = false;
            }
        }
        save.active = valid;
    }

    private void saveAndClose() {
        for (Column column : columns) {
            for (Field<?> field : column.fields) field.commit();
        }
        Config.SPEC.save();
        onClose();
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public void tick() {
        for (Column column : columns) {
            for (Field<?> field : column.fields) field.box.tick();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, top() - 43, 0xFFFFFF);
        for (Column column : columns) {
            graphics.drawCenteredString(font, column.name, column.fields[0].box.getX() + (COLUMN_WIDTH - 8) / 2, top() - 16, 0xFFFFFF);
        }
        for (Field<?> field : columns[0].fields) {
            graphics.drawString(font, field.label, left(), field.box.getY() + 5, 0xFFFFFF);
        }
        graphics.drawCenteredString(font, SERVER_NOTE, width / 2, top() + 76, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private record Column(Component name, Field<?>[] fields) {
        Column(Config.NecromancerSettings settings) {
            this(Component.translatable(settings.translationKey), new Field<?>[]{
                    new Field<>(settings.baseHealth, Double::valueOf),
                    new Field<>(settings.baseAttackDamage, Double::valueOf),
                    new Field<>(settings.summonLimit, Integer::valueOf)});
        }
    }

    private static final class Field<T extends Number> {
        private final ForgeConfigSpec.ConfigValue<T> value;
        private final ForgeConfigSpec.ValueSpec spec;
        private final Function<String, T> parse;
        private final Component label;
        private final Tooltip tooltip;
        private EditBox box;
        private T pending;

        Field(ForgeConfigSpec.ConfigValue<T> value, Function<String, T> parse) {
            this.value = value;
            this.parse = parse;
            spec = Config.SPEC.getSpec().get(value.getPath());
            String key = spec.getTranslationKey();
            label = Component.translatable(key);
            ForgeConfigSpec.Range<?> range = spec.getRange();
            tooltip = Tooltip.create(Component.translatable(KEY + ".details", Component.translatable(key + ".description"),
                    value.getDefault(), range.getMin(), range.getMax()));
        }

        boolean validate() {
            try {
                T number = parse.apply(box.getValue());
                pending = spec.test(number) ? number : null;
            } catch (NumberFormatException invalidInput) {
                pending = null;
            }
            box.setTextColor(pending == null ? 0xFF5555 : 0xE0E0E0);
            return pending != null;
        }

        void commit() { value.set(pending); }
    }
}
